package com.tokopedia.logisticcart.scheduledelivery.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesGqlResponse
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.AdditionalDeliveryData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.DeliveryProduct
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.DeliveryService
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.scheduledelivery.model.ScheduleDeliveryData
import com.tokopedia.logisticcart.scheduledelivery.model.ScheduleDeliveryParam
import com.tokopedia.logisticcart.scheduledelivery.model.ScheduleDeliveryResponse
import com.tokopedia.logisticcart.shipping.usecase.scheduleDeliveryQuery
import rx.Observable
import javax.inject.Inject

class GetScheduleDeliveryUseCase @Inject constructor(
    private val scheduler: SchedulerProvider
) {

    private var gql: GraphqlUseCase? = null

    fun execute(param: ScheduleDeliveryParam): Observable<ScheduleDeliveryResponse> {
        val query = scheduleDeliveryQuery()
        val gqlRequest = GraphqlRequest(query, RatesGqlResponse::class.java, param.toMap())
        // Need to init usecase here to prevent request cleared since this usecase will be called multiple time in a very tight interval of each call.
        // Will consider this as tech debt until find a proper solution
        val gql = GraphqlUseCase()
        this.gql = gql
        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
            .map { graphqlResponse: GraphqlResponse ->
//                val response: ScheduleDeliveryResponse =
//                    graphqlResponse.getData<ScheduleDeliveryResponse>(ScheduleDeliveryResponse::class.java)
//                        ?: ScheduleDeliveryResponse()
//                response
                mockResponse()
            }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql?.unsubscribe()
    }

    private fun mockResponse() : ScheduleDeliveryResponse {
        return ScheduleDeliveryResponse(
            scheduleDeliveryData = ScheduleDeliveryData(
                additionalDeliveryData = mockAdditionalDeliveryData()
            )
        )
    }

    private fun mockAdditionalDeliveryData(): AdditionalDeliveryData {
        return AdditionalDeliveryData(
            recommendAdditionalShipper = false,
            deliveryType = 0,
            available = true,
            hidden = false,
            title = "Jadwal lainnya",
            text = "Semua jadwal lain penuh dipesan",
            deliveryServices = arrayListOf(
                DeliveryService(
                    title = "Hari ini, 20 Sep",
                    titleLabel = "Hari ini",
                    id = "2022-09-20T00:00:00Z",
                    shipperId = 10,
                    available = true,
                    hidden = false,
                    deliveryProducts = arrayListOf(
                        DeliveryProduct(
                            title = "Tiba 14:00 - 16:00",
                            textEta = "Tiba hari ini, 14:00 - 16:00",
                            id = 2022092014123, // timeslot_id from schelly
                            finalPrice = 0.0,
                            realPrice = 10000.0,
                            textFinalPrice = "Rp0",
                            textRealPrice = "Rp10.000",
                            text = "Sisa 3 slot",
                            shipperId = 10,
                            shipperProductId = 28,
                            available = true,
                            hidden = false,
                            recommend = true,
                            promoCode = ""
                        ),
                        DeliveryProduct(
                            title = "Tiba 16:00 - 18:00",
                            textEta = "Tiba Besok, 08:00 - 10:00",
                            id = 2022092014124, // timeslot_id from schelly
                            finalPrice = 10000.0,
                            realPrice = 10000.0,
                            textFinalPrice = "Rp10.000",
                            textRealPrice = "Rp10.000",
                            text = "Sisa 3 slot",
                            shipperId = 10,
                            shipperProductId = 28,
                            available = true,
                            hidden = false,
                            recommend = false,
                            promoText = "Kuota gratis ongkirmu habis"
                        )
                    )
                )
            )
        )
    }
}
