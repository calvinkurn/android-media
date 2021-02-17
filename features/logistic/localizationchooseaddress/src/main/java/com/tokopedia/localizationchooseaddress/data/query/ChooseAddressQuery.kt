package com.tokopedia.localizationchooseaddress.data.query

object ChooseAddressQuery {

    val getChosenAddressList = """
        query keroAddrGetChosenAddressList {
          keroAddrGetChosenAddressList {
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
            }
            status
            server_process_time
            config
          }
        }

    """.trimIndent()
}