package com.tokopedia.logisticseller.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticseller.data.param.GetReschedulePickupParam
import com.tokopedia.logisticseller.data.response.GetReschedulePickupResponse
import javax.inject.Inject

@GqlQuery(
    GetReschedulePickupUseCase.GetReschedulePickupQuery,
    GetReschedulePickupUseCase.MP_LOGISTIC_GET_RESCHEDULE_PICKUP
)
class GetReschedulePickupUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetReschedulePickupParam, GetReschedulePickupResponse.Data>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return MP_LOGISTIC_GET_RESCHEDULE_PICKUP
    }

    override suspend fun execute(params: GetReschedulePickupParam): GetReschedulePickupResponse.Data {
//        return repository.request(GetReschedulePickupQuery(), params)
        return Gson().fromJson(DUMMY_RESPONSE, GetReschedulePickupResponse.Data::class.java)
    }

    companion object {
        const val GetReschedulePickupQuery = "GetReschedulePickupQuery"
        const val MP_LOGISTIC_GET_RESCHEDULE_PICKUP = """
            query GetReschedulePickup(${'$'}input:MpLogisticGetReschedulePickupInputs!){
                mpLogisticGetReschedulePickup(input:${'$'}input) {
                    app_link
                    order_detail_ticker
                    data{
                        shipper_id
                        shipper_name
      	                order_data{
                            order_id
        	                invoice
        	                shipper_product_id
                            shipper_product_name
        	                order_item{
                                name
                                qty
        	                }
                            choose_day{
                                day
                                choose_time{
                                    time
                                    eta_pickup
                                }
                            }
        	                choose_reason{
          	                    reason
        	                }
        	                error_message
    	                }
                    }
                }
            }
        """
        const val DUMMY_RESPONSE = """
            {
    "mpLogisticGetReschedulePickup": {
      "data": [
        {
          "shipper_id": 13,
          "shipper_name": "grab",
          "order_data": [
            {
              "order_id": 167100082,
              "invoice": "INV/20220406/MPL/20642990",
              "shipper_product_id": 37,
              "shipper_product_name": "instant",
              "order_item": [
                {
                  "name": "WELCOME TOKOPEDIA",
                  "qty": 1
                }
              ],
              "choose_day": [
                {
                  "day": "Selasa, 17 Mei 2022",
                  "choose_time": [
                    {
                      "time": "12:00",
                      "eta_pickup": "Estimasi Penjemputan <b>Instant Maksimal: Rabu, 6 Apr 2022,13:00 WIB</b>"
                    },
                    {
                      "time": "13:00",
                      "eta_pickup": "Estimasi Penjemputan <b>Instant Maksimal: Rabu, 6 Apr 2022,14:00 WIB</b>"
                    },
                    {
                      "time": "14:00",
                      "eta_pickup": "Estimasi Penjemputan <b>Instant Maksimal: Rabu, 6 Apr 2022,15:00 WIB</b>"
                    }
                  ]
                },
                {
                  "day": "Rabu, 18 Mei 2022",
                  "choose_time": [
                    {
                      "time": "08:00",
                      "eta_pickup": "Estimasi Penjemputan <b>Instant Maksimal: Kamis, 7 Apr 2022,09:00 WIB</b>"
                    },
                    {
                      "time": "09:00",
                      "eta_pickup": "Estimasi Penjemputan <b>Instant Maksimal: Kamis, 7 Apr 2022,10:00 WIB</b>"
                    },
                    {
                      "time": "10:00",
                      "eta_pickup": "Estimasi Penjemputan <b>Instant Maksimal: Kamis, 7 Apr 2022,11:00 WIB</b>"
                    },
                    {
                      "time": "11:00",
                      "eta_pickup": "Estimasi Penjemputan <b>Instant Maksimal: Kamis, 7 Apr 2022,12:00 WIB</b>"
                    },
                    {
                      "time": "12:00",
                      "eta_pickup": "Estimasi Penjemputan <b>Instant Maksimal: Kamis, 7 Apr 2022,13:00 WIB</b>"
                    },
                    {
                      "time": "13:00",
                      "eta_pickup": "Estimasi Penjemputan <b>Instant Maksimal: Kamis, 7 Apr 2022,14:00 WIB</b>"
                    },
                    {
                      "time": "14:00",
                      "eta_pickup": "Estimasi Penjemputan <b>Instant Maksimal: Kamis, 7 Apr 2022,15:00 WIB</b>"
                    }
                  ]
                }
              ],
              "choose_reason": [
                {
                  "reason": "Toko tutup"
                },
                {
                  "reason": "Pembeli Tidak Ditempat"
                },
                {
                  "reason": "Lainnya (Isi Sendiri)"
                }
              ],
              "error_message": ""
            }
          ]
        }
      ],
      "order_detail_ticker": "Pastikan pesanan yang mau diubah sudah benar.<br/>Ubah jadwal hanya bisa dilakukan sekali.",
      "app_link": "https://www.tokopedia.com/help/article/cara-mencari-driver-baru-untuk-layanan-instant-courier",
      "status": 0,
      "message_error": ""
    }
  }
        """
    }
}
