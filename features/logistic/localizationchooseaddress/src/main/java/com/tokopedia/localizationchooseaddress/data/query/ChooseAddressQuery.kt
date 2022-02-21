package com.tokopedia.localizationchooseaddress.data.query

object ChooseAddressQuery {

    val getChosenAddressList = """
        query keroAddrGetChosenAddressList(${'$'}source: String!, ${'$'}is_tokonow_request: Boolean!) {
          keroAddrGetChosenAddressList(source: ${'$'}source, is_tokonow_request: ${'$'}is_tokonow_request) {
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
              is_state_chosen_address
              tokonow {
                warehouse_id
                coverage_label
              }
            }
            status
            server_process_time
            config
          }
        }

    """.trimIndent()

    val setStateChosenAddress = """
        mutation setStateChosenAddress(${'$'}input : KeroAddrSetStateChosenAddressInput!) {
          keroAddrSetStateChosenAddress(input: ${'$'}input) {
            data{
              is_success
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

    val getStateChosenAddress = """
        query KeroAddrGetStateChosenAddress(${'$'}source: String!, ${'$'}is_tokonow_request: Boolean!){
          keroAddrGetStateChosenAddress(source: ${'$'}source, is_tokonow_request: ${'$'}is_tokonow_request) {
            data {
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

    val getDefaultChosenAddress = """
        query KeroAddrGetDefaultChosenAddress(${'$'}lat_long: String!,  ${'$'}source: String!,  ${'$'}is_tokonow_request: Boolean!){
          keroAddrGetDefaultChosenAddress(input: {lat_long: ${'$'}lat_long, source: ${'$'}source, is_tokonow_request: ${'$'}is_tokonow_request}) {
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
            tokonow {
              shop_id
              warehouse_id
              warehouses {
                warehouse_id
                service_type
              }
              service_type
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
}