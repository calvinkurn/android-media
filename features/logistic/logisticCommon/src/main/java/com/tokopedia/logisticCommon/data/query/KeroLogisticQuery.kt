package com.tokopedia.logisticCommon.data.query

object KeroLogisticQuery {

    val autoComplete = """
        query KeroMapsAutoComplete(${'$'}param: String!) {
          kero_maps_autocomplete(input: ${'$'}param) {
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
            }
            token {
              district_recommendation
              ut
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
        query kero_maps_autofill(${'$'}latlng: String!){
          kero_maps_autofill(latlng: ${'$'}latlng) {
            data {
              title
              formatted_address
              city_id
              province_id
              district_id
              district_name
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

}