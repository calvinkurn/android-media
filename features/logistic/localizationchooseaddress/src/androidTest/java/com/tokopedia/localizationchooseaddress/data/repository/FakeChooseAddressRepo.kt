package com.tokopedia.localizationchooseaddress.data.repository

import com.google.gson.Gson
import com.tokopedia.localizationchooseaddress.domain.response.GetChosenAddressListQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressGqlResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import javax.inject.Inject


class FakeChooseAddressRepo @Inject constructor() : ChooseAddressRepo {

    override suspend fun getChosenAddressList(): GetChosenAddressListQglResponse {
        return Gson().fromJson(dummyChosenAddressResponse, GetChosenAddressListQglResponse::class.java)
    }

    override suspend fun setStateChosenAddress(status: Int, addressId: String, receiverName: String, addressName: String, latitude: String, longitude: String, districtId: String, postalCode: String): SetStateChosenAddressQqlResponse {
        return Gson().fromJson(dummySetChosenAddressResponse, SetStateChosenAddressQqlResponse::class.java)
    }

    override suspend fun getStateChosenAddress(source: String): GetStateChosenAddressQglResponse {
        return Gson().fromJson(dummyGetChosenAddressResponse, GetStateChosenAddressQglResponse::class.java)
    }

    override suspend fun getDefaultChosenAddress(latLong: String?, source: String): GetDefaultChosenAddressGqlResponse {
        return Gson().fromJson(dummyGetDefaultAddressResponse, GetDefaultChosenAddressGqlResponse::class.java)
    }


}

val dummyChosenAddressResponse = """
    {
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
          "status": "",
          "server_process_time": "",
          "config": ""
        }
      }
""".trimIndent()

val dummySetChosenAddressResponse = """
    {
        "keroAddrSetStateChosenAddress": {
          "data": {
              "is_success": 1,
              "chosen_address": {
                "addr_id": 4683624,
                "receiver_name": "Joel Hutasoit",
                "addr_name": "Rumah",
                "district": 2270,
                "city": 175,
                "city_name": "Jakarta Selatan",
                "district_name": "Setiabudi",
                "status": 1,
                "latitude": "-6.22119739999998",
                "longitude": "106.81941940000002",
                "postal_code": "12950"
              }
          },
          "status": "OK",
          "server_process_time": "",
          "config": ""
        }
      }
""".trimIndent()

val dummyGetChosenAddressResponse = """
    {
        "keroAddrGetStateChosenAddress": {
          "data": 
          {
                "addr_id": 4683624,
                "receiver_name": "Joel Hutasoit",
                "addr_name": "Rumah",
                "district": 2270,
                "city": 175,
                "city_name": "Jakarta Selatan",
                "district_name": "Setiabudi",
                "status": 1,
                "latitude": "-6.22119739999998",
                "longitude": "106.81941940000002",
                "postal_code": "12950"
                "error": {
                    "code": 0,
                    "title": ""
                }
          },
          "status": "OK",
          "server_process_time": "",
          "config": ""
        }
      }
""".trimIndent()

val dummyGetDefaultAddressResponse = """
    {
        "keroAddrGetDefaultChosenAddress": {
          "data": 
          {
              "addr_id": 4693275,
              "receiver_name": "Hermawan",
              "addr_name": "Office",
              "address_1": "Setiabudi 123 123 123 123",
              "address_2": "-6.222363417323176,106.81781548421694",
              "postal_code": "12910",
              "province": 13,
              "city": 175,
              "district": 2270,
              "phone": "628123123123123",
              "province_name": "Jawa Timur",
              "city_name": "Kab. Bojonegoro",
              "district_name": "Sumberejo",
              "status": 4,
              "country": "",
              "latitude": "-7.230790",
              "longitude": "112.041080"
            },
            "kero_addr_error": {
              "code": 0,
              "detail": ""
            },
          "status": "OK",
          "server_process_time": "",
          "config": ""
        }
      }
""".trimIndent()