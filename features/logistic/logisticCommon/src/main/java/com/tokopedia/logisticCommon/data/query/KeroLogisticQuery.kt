package com.tokopedia.logisticCommon.data.query

object KeroLogisticQuery {

    val autoComplete = """
        query KeroMapsAutoComplete(${'$'}param: String!, ${'$'}latlng: String) {
          kero_maps_autocomplete(input: ${'$'}param, latlng: ${'$'}latlng) {
            error_code
            data {
              predictions {
                description
                place_id
                types
                matched_substrings {
                  length
                  offset
                }
                terms {
                  value
                  offset
                }
                structured_formatting {
                  main_text
                  main_text_matched_substrings {
                    length
                    offset
                  }
                  secondary_text
                }
              }
            }
          }
        }
        """.trimIndent()

    val addressCorner = """
        query keroAddressCorner(${'$'}input: KeroGetAddressInput){
          keroAddressCorner(input:${'$'}input) {
            status
            config
            server_process_time
            data {
              addr_id
              receiver_name
              addr_name
              address_1
              address_2
              postal_code
              province
              city
              district
              phone
              country
              province_name
              city_name
              district_name
              latitude
              longitude
              status
              is_primary
              is_active
              is_whitelist
              partner_id
              partner_name
              type
              is_corner
              is_state_chosen_address
              radio_button_checked
              is_shared_address
            }
            token {
              district_recommendation
              ut
            }
            page_info {
              ticker
              button_label
            }
          }
        }
    """.trimIndent()

    val placesGetDistrict = """
        query KeroPlacesGetDistrict(${'$'}param: String!, ${'$'}err: Boolean) {
          kero_places_get_district(placeid: ${'$'}param, error_data: ${'$'}err) {
            error_code
            data {
              title
              formatted_address
              district_id
              city_id
              province_id
              district_name
              city_name
              province_name
              postal_code
              latitude
              longitude
              full_data {
                long_name
                short_name
                types
              }
            }
            status
            message_error
          }
        }
    """.trimIndent()

    val getDistrictDetails = """
        query KeroDistrictQuery(${'$'}query: String, ${'$'}page: String){
          keroGetDistrictDetails(query:${'$'}query, page:${'$'}page) {
            district {
              district_id
              district_name
              city_id
              city_name
              province_id
              province_name
              zip_code
            }
            next_available
          }
        }
    """.trimIndent()

    val keroMapsAutofill = """
        query kero_maps_autofill(${'$'}latlng: String!, ${'$'}err: Boolean){
          kero_maps_autofill(latlng: ${'$'}latlng, error_data: ${'$'}err) {
            data {
              title
              formatted_address
              city_id
              province_id
              province_name
              district_id
              district_name
              city_name
              postal_code
              latitude
              longitude
              full_data {
                long_name
                short_name
                types
              }
            }
            status
            message_error
            error_code
          }
        }
    """.trimIndent()

    val kero_add_address_query = """
        mutation Autofill(${'$'}input: KeroAgentAddressInput!) {
          kero_add_address(input: ${'$'}input) {
            data {
              addr_id
              is_success
              is_state_chosen_address_changed
              chosen_address { 
                addr_id
                receiver_name
                addr_name
                district
                city
                city_name
                district_name
                status
                latitude
                longitude
                postal_code
                }
              tokonow {
                shop_id
                warehouse_id
                warehouses {
                    warehouse_id
                    service_type
                }
                service_type
                }
            }
            status
            config
            server_process_time
          }
        }
        """.trimIndent()

    val kero_addr_get_default = """
        query KeroAddrGetDefaultAddress(${'$'}source: String!) {
          KeroAddrGetDefaultAddress(source: ${'$'}source) {
            data {
              addr_id
              receiver_name
              addr_name
              address_1
              address_2
              postal_code
              province
              city
              district
              phone
              province_name
              city_name
              district_name
              status
              country
              latitude
              longitude
            }
            kero_addr_error {
              code
              detail
            }
            status
            server_process_time
            config
          }
        }
    """.trimIndent()

    val kero_district_boundary = """
        query keroGetDistrictBoundaryArray(${'$'}districtId: Int!) {
          keroGetDistrictBoundaryArray(input: {
            district_id: ${'$'}districtId
          }) {
            type
            properties {
              id
              name
            }
            geometry {
              type
              coordinates
              crs {
                type
                properties {
                  id
                  name
                }
              }
            }
          }
        }

    """.trimIndent()

    val district_recommendation = """
        query GetDistrictRecommendation(${'$'}query: String, ${'$'}page: String){
          kero_district_recommendation(query:${'$'}query, page:${'$'}page) {
            district {
              district_id
              district_name
              city_id
              city_name
              province_id
              province_name
              zip_code
            }
            next_available
          }
        }
    """.trimIndent()

    val eligible_for_address_feature = """
        query eligibleForAddressFeature(${'$'}feature_id: Int!, ${'$'}device: String!, ${'$'}device_version: String!){ 
          KeroAddrIsEligibleForAddressFeature(feature_id:${'$'}feature_id, device:${'$'}device, device_version:${'$'}device_version) {
            data {
              eligible
            }
            kero_addr_error {
              code
              detail
            }
            status
            server_process_time
            config
          }
      }
    """.trimIndent()

    val kero_addr_get_district_center = """
        query kero_addr_get_district_center(${'$'}districtId: Int!) {
            kero_addr_get_district_center(districtId:${'$'}districtId) {
                district {
                    district_id
                    latitude
                    longitude
                }
            }
        }
    """.trimIndent()


    val kero_get_address_detail = """
        query getAddressDetail(${'$'}input: KeroGetAddressInput!){
            kero_get_address(input: ${'$'}input) {
                data {
                  addr_id
                  receiver_name
                  addr_name
                  address_1
                  address_2
                  postal_code
                  province
                  city
                  district
                  phone
                  province_name
                  city_name
                  district_name
                  status
                  country
                  latitude
                  longitude
                  is_primary
                  is_active
                  is_whitelist
                  address_detail_street
                  address_detail_notes
                }
                status
                server_process_time
                config
            }
        }
    """.trimIndent()

    val kero_edit_address = """
        mutation editAddress(${'$'}input:KeroAddressInput!) {
          kero_edit_address(input:${'$'}input) {
            data{
              is_success
              is_state_chosen_address_changed
            }
            status
            config
            server_process_time
          }
        }
    """.trimIndent()

    val pinpoint_validation = """
        mutation pinpoint_validation(${'$'}district_id: Int, ${'$'}latitude: String, ${'$'}longitude: String, ${'$'}postal_code: String) {
            pinpoint_validation(district_id: ${'$'}district_id, latitude: ${'$'}latitude, longitude: ${'$'}longitude, postal_code: ${'$'}postal_code) {
                data {
                    checksum
                    district_id
                    latitude
                    longitude
                    result
                    result_text
                }
            }
        }
    """.trimIndent()
}
