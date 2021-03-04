package com.tokopedia.manageaddress

import com.google.gson.Gson
import com.tokopedia.logisticCommon.domain.mapper.AddressCornerMapper
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.response.GetPeopleAddressResponse

object AddressDummyDataProvider {

    fun getAddressList(): AddressListModel {
        val mapper = AddressCornerMapper()
        val response: GetPeopleAddressResponse = Gson().fromJson(cornerListResponse, GetPeopleAddressResponse::class.java)
        return mapper.call(response)
    }

}

val cornerListResponse = """
{
    "keroAddressCorner": {
      "status": "OK",
      "config": "",
      "server_process_time": "",
      "data": [
        {
          "addr_id": 4655257,
          "receiver_name": "Corner Receiver",
          "addr_name": "Barat Syar'i Mart (Ex Apple Store)",
          "address_1": "Jl. Kaliurang KM 14,5",
          "address_2": "999,999",
          "postal_code": "55584",
          "province": 16,
          "city": 256,
          "district": 3601,
          "phone": "",
          "country": "Indonesia",
          "province_name": "D.I. Yogyakarta",
          "city_name": "Kab. Sleman",
          "district_name": "Ngemplak",
          "latitude": "999",
          "longitude": "999",
          "status": 1,
          "is_primary": false,
          "is_active": false,
          "is_whitelist": false,
          "partner_id": 4,
          "partner_name": "Test3",
          "type": 2,
          "is_corner": true
        }
      ],
      "token": {
        "district_recommendation": "Tokopedia+Kero:YYKDnhTpS6qQAdgPJI2ctTff/VM=",
        "ut": "1557474983"
      }
    }
}
""".trimIndent()