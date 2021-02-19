package com.tokopedia.localizationchooseaddress.data.repository

import com.google.gson.Gson
import com.tokopedia.localizationchooseaddress.domain.response.GetChosenAddressListQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import javax.inject.Inject

class FakeChooseAddressRepo @Inject constructor() : ChooseAddressRepo {

    override suspend fun getChosenAddressList(): GetChosenAddressListQglResponse {
        return Gson().fromJson(dummyChosenAddressResponse, GetChosenAddressListQglResponse::class.java)
    }

}

val dummyChosenAddressResponse = """
    {
      "data": {
        "keroAddrGetChosenAddressList": {
          "data": [
            {
              "addr_id": 4725085,
              "receiver_name": "abc",
              "addr_name": "testgql by chetan 123132",
              "address_1": "jalan jal chetan an jalan jalan 123",
              "address_2": "",
              "postal_code": "15220",
              "province": 11,
              "city": 463,
              "district": 1613,
              "phone": "62812382183128",
              "province_name": "Banten",
              "city_name": "Kota Tangerang Selatan",
              "district_name": "Pondok Aren",
              "status": 2,
              "country": "Indonesia",
              "latitude": "",
              "longitude": "",
              "is_state_chosen_address": false
            },
            {
              "addr_id": 4683624,
              "receiver_name": "Joel Hutasoit",
              "addr_name": "Rumah",
              "address_1": "Tokopedia Tower, Jalan Professor Doktor Satrio, Karet Semanggi, South Jakarta City, Jakarta, Indonesia[Tokopedia notes: lantai 49 tokopedia tower]",
              "address_2": "-6.22119739999998,106.81941940000002",
              "postal_code": "12950",
              "province": 13,
              "city": 175,
              "district": 2270,
              "phone": "6287851117744",
              "province_name": "DKI Jakarta",
              "city_name": "Jakarta Selatan",
              "district_name": "Setiabudi",
              "status": 1,
              "country": "Indonesia",
              "latitude": "-6.22119739999998",
              "longitude": "106.81941940000002",
              "is_state_chosen_address": false
            }
          ],
          "kero_addr_error": {
            "code": 0,
            "detail": ""
          },
          "status": "",
          "server_process_time": "",
          "config": ""
        }
      }
    }
""".trimIndent()