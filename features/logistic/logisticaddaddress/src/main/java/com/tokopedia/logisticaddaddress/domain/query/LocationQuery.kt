package com.tokopedia.logisticaddaddress.domain.query

import com.google.gson.Gson
import com.tokopedia.logisticaddaddress.domain.model.dropoff.GetStoreResponse

object LocationQuery {
    val keroAddressStoreLocation = """
        query KeroAddressStoreLocation(${'$'}query: KeroAddressStoreLocationInput) {
          keroAddressStoreLocation(input: (${'$'}query) {
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

    val getStoreDummyJson = """
        {
            "keroAddressStoreLocation": {
              "status": "OK",
              "config": "",
              "server_process_time": "",
              "data": [
                {
                  "addr_id": 4674576,
                  "receiver_name": "Trade In Receiver",
                  "addr_name": "DR SATRIO 289",
                  "address_1": "JL. PROF. DR. SATRIO N0. 289",
                  "address_2": "-6.2177778,106.8194444",
                  "postal_code": "12920",
                  "province": 13,
                  "city": 175,
                  "district": 2270,
                  "phone": "08881002309",
                  "country": "Indonesia",
                  "province_name": "DKI Jakarta",
                  "city_name": "Jakarta Selatan",
                  "district_name": "Setiabudi",
                  "latitude": "-6.2177778",
                  "longitude": "106.8194444",
                  "status": 1
                },
                {
                  "addr_id": 4674577,
                  "receiver_name": "Trade In Receiver",
                  "addr_name": "APARTEMENT PEARL GARDEN",
                  "address_1": "APT. PEARL GARDEN JL. GATOT SUBROTO KAV. VI-7",
                  "address_2": "-6.2227778,106.8163889",
                  "postal_code": "12930",
                  "province": 13,
                  "city": 175,
                  "district": 2270,
                  "phone": "08881452476",
                  "country": "Indonesia",
                  "province_name": "DKI Jakarta",
                  "city_name": "Jakarta Selatan",
                  "district_name": "Setiabudi",
                  "latitude": "-6.2227778",
                  "longitude": "106.8163889",
                  "status": 1
                },
                {
                  "addr_id": 4674192,
                  "receiver_name": "Trade In Receiver",
                  "addr_name": "RS JAKARTA",
                  "address_1": "JL. GARNISUN NO. 1",
                  "address_2": "-6.2183333,106.8163889",
                  "postal_code": "12930",
                  "province": 13,
                  "city": 175,
                  "district": 2270,
                  "phone": "08818059659",
                  "country": "Indonesia",
                  "province_name": "DKI Jakarta",
                  "city_name": "Jakarta Selatan",
                  "district_name": "Setiabudi",
                  "latitude": "-6.2183333",
                  "longitude": "106.8163889",
                  "status": 1
                },
                {
                  "addr_id": 4674578,
                  "receiver_name": "Trade In Receiver",
                  "addr_name": "BEK MURAD 36",
                  "address_1": "JL. BEH MURAD NO. 36",
                  "address_2": "-6.2161111,106.82",
                  "postal_code": "12940",
                  "province": 13,
                  "city": 175,
                  "district": 2270,
                  "phone": "08111357021",
                  "country": "Indonesia",
                  "province_name": "DKI Jakarta",
                  "city_name": "Jakarta Selatan",
                  "district_name": "Setiabudi",
                  "latitude": "-6.2161111",
                  "longitude": "106.82",
                  "status": 1
                },
                {
                  "addr_id": 4674579,
                  "receiver_name": "Trade In Receiver",
                  "addr_name": "KARET PEDURENAN",
                  "address_1": "JL. KARET PENDURENAN MASJID III NO:50 RT 08/04",
                  "address_2": "-6.2222222,106.8258333",
                  "postal_code": "12950",
                  "province": 13,
                  "city": 175,
                  "district": 2270,
                  "phone": "088809883750",
                  "country": "Indonesia",
                  "province_name": "DKI Jakarta",
                  "city_name": "Jakarta Selatan",
                  "district_name": "Setiabudi",
                  "latitude": "-6.2222222",
                  "longitude": "106.8258333",
                  "status": 1
                }
              ]
            }
        }
    """.trimIndent()

    val getStoreDummyObject = Gson().fromJson(getStoreDummyJson, GetStoreResponse::class.java)

}
