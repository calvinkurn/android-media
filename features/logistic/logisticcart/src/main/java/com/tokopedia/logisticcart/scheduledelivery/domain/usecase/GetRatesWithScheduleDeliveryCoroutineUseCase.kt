package com.tokopedia.logisticcart.scheduledelivery.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.mapper.ScheduleDeliveryMapper
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.usecase.GetRatesCoroutineUseCase
import javax.inject.Inject

class GetRatesWithScheduleDeliveryCoroutineUseCase @Inject constructor(
    private val getRatesCoroutineUseCase: GetRatesCoroutineUseCase,
    private val getScheduleDeliveryUseCase: GetScheduleDeliveryCoroutineUseCase,
    private val mapper: ScheduleDeliveryMapper,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Pair<RatesParam, String>, ShippingRecommendationData>(dispatcher.io) {

    override suspend fun execute(params: Pair<RatesParam, String>): ShippingRecommendationData {
        val ratesResponse = getRatesCoroutineUseCase(params.first)
        val schellyResponse = getScheduleDeliveryUseCase(mapper.map(params.first, params.second))
        ratesResponse.scheduleDeliveryData = schellyResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
        return ratesResponse
    }

    override fun graphqlQuery(): String {
        return ""
    }
}
