/*-
 * ---license-start
 * EU Digital Green Certificate Gateway Service / dgc-lib
 * ---
 * Copyright (C) 2021 T-Systems International GmbH and all other contributors
 * ---
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ---license-end
 */

package eu.europa.ec.dgc.gateway.connector.client;

import eu.europa.ec.dgc.gateway.connector.dto.CertificateTypeDto;
import eu.europa.ec.dgc.gateway.connector.dto.TrustListItemDto;
import eu.europa.ec.dgc.gateway.connector.dto.ValidationRuleDto;
import java.util.List;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@ConditionalOnProperty("dgc.gateway.connector.enabled")
@FeignClient(
    name = "dgc-gateway-connector",
    url = "${dgc.gateway.connector.endpoint}",
    configuration = DgcGatewayConnectorRestClientConfig.class
)
public interface DgcGatewayConnectorRestClient {

    /**
     * Gets the trusted certificates from digital green certificate gateway.
     *
     * @param type The type to filter for (e.g. CSCA, UPLOAD, AUTHENTICATION, DSC)
     * @return List of trustListItems
     */
    @GetMapping(value = "/trustList/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<TrustListItemDto>> getTrustedCertificates(@PathVariable("type") CertificateTypeDto type);

    /**
     * Uploads a new Signer Certificate to digital green certificate gateway.
     *
     * @param cmsSignedCertificate CMS Signed Certificate Message.
     * @return ResponseEntity with upload result.
     */
    @PostMapping(value = "/signerCertificate", consumes = "application/cms")
    ResponseEntity<Void> uploadSignerInformation(@RequestBody String cmsSignedCertificate);

    /**
     * Deletes a Signer Certificate from digital green certificate gateway.
     *
     * @param cmsSignedCertificate CMS Signed Certificate Message.
     * @return ResponseEntity with delete result.
     */
    @DeleteMapping(value = "/signerCertificate", consumes = "application/cms")
    ResponseEntity<Void> deleteSignerInformation(@RequestBody String cmsSignedCertificate);

    /**
     * Downloads the Countrylist from digital green certificate gateway.
     *
     * @return List of Strings (2 Digit Country Codes)
     */
    @GetMapping(value = "/countrylist", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<String>> downloadCountryList();

    /**
     * Downloads the list of available ValueSets from digital green certificate gateway.
     *
     * @return List of Strings
     */
    @GetMapping(value = "/valuesets", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<String>> downloadValueSetIds();

    /**
     * Downloads a ValueSet by its id from digital green certificate gateway.
     *
     * @return the JSON Representation of the ValueSet.
     */
    @GetMapping(value = "/valuesets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> downloadValueSet(@PathVariable("id") String id);

    /**
     * Uploads a new ValidationRule to digital green certificate gateway.
     *
     * @param validationRule the CMS signed ValidationRule JSON.
     */
    @PostMapping(value = "/rules", consumes = "application/cms-text")
    ResponseEntity<Void> uploadValidationRule(@RequestBody String validationRule);

    /**
     * Deletes a ValidationRule from digital green certificate gateway.
     *
     * @param validationRuleId the CMS signed ValidationRule Identifier.
     */
    @DeleteMapping(value = "/rules", consumes = "application/cms-text")
    ResponseEntity<Void> deleteValidationRule(@RequestBody String validationRuleId);

    /**
     * Downloads a Validation Rule from digital green certificate gateway.
     *
     * @param countryCode of the Validation Rule
     * @return JSON Structure containing relevant versions of Validation Rule
     */
    @GetMapping(value = "/rules/{cc}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<String, List<ValidationRuleDto>>> downloadValidationRule(@PathVariable("cc") String countryCode);

}
