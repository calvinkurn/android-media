package com.tokopedia.logisticCommon.data.query

object KeroLogisticQuery {

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

    val keroMapsAutofill = """
        query kero_maps_autofill(${'$'}latlng: String!, ${'$'}err: Boolean, ${'$'}is_manage_address_flow: Boolean){
          kero_maps_autofill(latlng: ${'$'}latlng, error_data: ${'$'}err, is_manage_address_flow: ${'$'}is_manage_address_flow) {
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

    val kero_edit_address = """
        mutation editAddress(${'$'}input:KeroAddressInput!) {
          kero_edit_address(input:${'$'}input) {
            data{
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
