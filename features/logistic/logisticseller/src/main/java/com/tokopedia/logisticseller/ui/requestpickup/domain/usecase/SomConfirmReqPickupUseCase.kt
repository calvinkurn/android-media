package com.tokopedia.logisticseller.ui.requestpickup.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomConfirmReqPickupParam
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.logisticseller.ui.requestpickup.domain.query.SomConfirmReqPickupQuery
import javax.inject.Inject

/**
 * Created by fwidjaja on 12/05/20.
 */
class SomConfirmReqPickupUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomConfirmReqPickup.Data>) {

    init {
        useCase.setTypeClass(SomConfirmReqPickup.Data::class.java)
    }

    suspend fun execute(param: SomConfirmReqPickupParam): SomConfirmReqPickup.Data {
        useCase.setGraphqlQuery(SomConfirmReqPickupQuery)
        useCase.setRequestParams(generateParam(param))
        return useCase.executeOnBackground()
//        return Gson().fromJson(dummyData, SomConfirmReqPickup.Data::class.java)
//
    }

    private fun generateParam(param: SomConfirmReqPickupParam): Map<String, Any?> {
        return mapOf(LogisticSellerConst.PARAM_INPUT to param)
    }

    val dummyData = """
    
        {
              "mpLogisticPreShipInfo": {
                "status": "",
                "message_error": [],
                "data": {
                  "pickup_location": {
                    "title": "Lokasi Pengambilan",
                    "address": "[DUMMY]Karet, Kecamatan Setiabudi",
                    "phone": "6285780407555"
                  },
                  "drop_off_location": {
                        "drop_off_courier": [
                            {
                                "courier_id":1,
                                "courier_name":"JNE"
                            }
                        ],
                        "drop_off_notes": {
                            "title": "Bisa drop off di gerai, lho!",
                            "text": "Jika kurir belum pickup pesananmu, kamu juga bisa drop off langsung ke gerai terdekat dari tokomu.",
                            "url_text": "Cek Gerai Terdekat",
                            "url_detail": "www.tokopedia.com/link_drop_off_point_might_be_different_per_order"
                        }
		},
                  "detail": {
                    "title": "Detail pengajuan pickup pesanan",
                    "invoice": "INV/20230606/MPL/20818845",
                    "shippers": [
                      {
                        "name": "REX",
                        "service": "(REX-10)",
                        "note": "Batas Waktu Penjemputan Selasa, 5 Sep 09:20 WIB",
                        "courier_image": "https://images.tokopedia.net/img/kurir-rex.png",
                        "count_text": "Jumlah: ",
                        "count": "1"
                      }
                    ],
                    "orchestra_partner": ""
                  },
                  "notes": {
                    "title": "Pastikan: ",
                    "list": [
                      "Pesanan sudah terbungkus rapi",
                      "Kode booking, alamat, dan nomor telepon tertulis di kemasan paket",
                      "Tidak perlu memberikan ongkir ke kurir"
                    ]
                  },
                  "schedule_time_day": {
                    "today": [],
                    "tomorrow": []
                  },
                  "ticker": {
                    "text": "",
                    "url_text": "",
                    "url_detail": "",
                    "action_key": "",
                    "type": ""
                  }
                }
              }
            }
          
        
     
        
    """.trimIndent()

}


