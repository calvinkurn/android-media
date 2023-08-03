package com.tokopedia.logisticcart.scheduledelivery.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.entity.response.ScheduleDeliveryRatesResponse
import com.tokopedia.logisticcart.scheduledelivery.domain.mapper.ScheduleDeliveryMapper
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.usecase.GetRatesCoroutineUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetRatesWithScheduleDeliveryCoroutineUseCase @Inject constructor(
    private val getRatesCoroutineUseCase: GetRatesCoroutineUseCase,
    private val getScheduleDeliveryUseCase: GetScheduleDeliveryCoroutineUseCase,
    private val mapper: ScheduleDeliveryMapper,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Pair<RatesParam, String>, ShippingRecommendationData>(dispatcher.io) {

    override suspend fun execute(params: Pair<RatesParam, String>): ShippingRecommendationData {
        return coroutineScope {
            val shippingRecommendationData = ShippingRecommendationData()
            val ratesResponse = async { getRatesCoroutineUseCase(params.first) }
            val schellyResponse = async { getScheduleDeliveryUseCase(mapper.map(params.first, params.second)) }
            shippingRecommendationData.combine(ratesResponse.await(), schellyResponse.await())
            return@coroutineScope shippingRecommendationData
        }
    }
    private fun ShippingRecommendationData.combine(ratesResponse: ShippingRecommendationData, scheduleDeliveryData: ScheduleDeliveryRatesResponse) {
        this.shippingDurationUiModels = ratesResponse.shippingDurationUiModels
        this.logisticPromo = ratesResponse.logisticPromo
        this.listLogisticPromo = ratesResponse.listLogisticPromo
        this.productShipmentDetailModel = ratesResponse.productShipmentDetailModel
        this.errorMessage = ratesResponse.errorMessage
        this.errorId = ratesResponse.errorId
        this.scheduleDeliveryData = scheduleDeliveryData.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
    }

    override fun graphqlQuery(): String {
        return ""
    }
}
