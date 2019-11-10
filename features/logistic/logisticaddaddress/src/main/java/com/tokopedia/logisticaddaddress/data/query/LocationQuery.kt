package com.tokopedia.logisticaddaddress.data.query

import com.google.gson.Gson
import com.tokopedia.logisticaddaddress.data.entity.response.GetStoreResponse

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
          "status": 1,
          "type": 3,
          "store_code": "TJC5",
          "opening_hours": "EVERYDAY (07.00-22.00)",
          "store_distance": "0.3628873"
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
          "status": 1,
          "type": 3,
          "store_code": "FLIR",
          "opening_hours": "EVERYDAY (07.00-22.00)",
          "store_distance": "0.4900919"
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
          "status": 1,
          "type": 3,
          "store_code": "TVVL",
          "opening_hours": "OFFICE HOUR",
          "store_distance": "0.5273035"
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
          "status": 1,
          "type": 3,
          "store_code": "TLJ2",
          "opening_hours": "EVERYDAY (07.00-22.00)",
          "store_distance": "0.534334"
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
          "status": 1,
          "type": 3,
          "store_code": "T13Q",
          "opening_hours": "EVERYDAY (07.00-22.00)",
          "store_distance": "0.6183302"
        }
      ],
      "global_radius": 5
    }
  }
""".trimIndent()

    val getStoreDummyObject = Gson().fromJson(getStoreDummyJson, GetStoreResponse::class.java)

}
