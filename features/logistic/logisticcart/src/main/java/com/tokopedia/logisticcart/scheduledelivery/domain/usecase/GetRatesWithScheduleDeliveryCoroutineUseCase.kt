package com.tokopedia.logisticcart.scheduledelivery.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
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
            shippingRecommendationData.combineWithRates(ratesResponse.await())
            shippingRecommendationData.scheduleDeliveryData = schellyResponse.await().ongkirGetScheduledDeliveryRates.scheduleDeliveryData
            return@coroutineScope shippingRecommendationData
        }
    }
    private fun ShippingRecommendationData.combineWithRates(ratesResponse: ShippingRecommendationData) {
        this.shippingDurationUiModels = ratesResponse.shippingDurationUiModels
        this.logisticPromo = ratesResponse.logisticPromo
        this.listLogisticPromo = ratesResponse.listLogisticPromo
        this.preOrderModel = ratesResponse.preOrderModel
        this.errorMessage = ratesResponse.errorMessage
        this.errorId = ratesResponse.errorId
    }

    override fun graphqlQuery(): String {
        return ""
    }
}
