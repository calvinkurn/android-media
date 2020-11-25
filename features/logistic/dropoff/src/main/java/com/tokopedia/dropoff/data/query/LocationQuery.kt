package com.tokopedia.dropoff.data.query

import com.google.gson.Gson
import com.tokopedia.dropoff.data.response.getStore.GetStoreResponse

object LocationQuery {
    val keroAddressStoreLocation = """
        query KeroAddressStoreLocation(${'$'}query: KeroAddressStoreLocationInput) {
          keroAddressStoreLocation(input: ${'$'}query) {
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
              type
              store_code
              opening_hours
              store_distance
            }
            global_radius
          }
        }
    """.trimIndent()

}
