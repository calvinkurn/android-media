package com.tokopedia.logisticcart.scheduledelivery.usecase

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.scheduledelivery.model.ScheduleDeliveryParam
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import rx.Observable
import javax.inject.Inject

class GetRatesWithScheduleUseCase @Inject constructor(
    private val getRatesUseCase: GetRatesUseCase,
    private val getScheduleDeliveryUseCase: GetScheduleDeliveryUseCase,
    private val scheduler: SchedulerProvider
) {

    private var gql: GraphqlUseCase? = null

    fun execute(ratesParam: RatesParam, scheduleDeliveryParam: ScheduleDeliveryParam): Observable<ShippingRecommendationData> {
        return Observable.just(ShippingRecommendationData())
            .flatMap { shippingRecommendationData ->
                getRatesUseCase.execute(ratesParam)
                    .map {
                        shippingRecommendationData.shippingDurationUiModels = it.shippingDurationUiModels
                        shippingRecommendationData.logisticPromo = it.logisticPromo
                        shippingRecommendationData.listLogisticPromo = it.listLogisticPromo
                        shippingRecommendationData.preOrderModel = it.preOrderModel
                        shippingRecommendationData.errorMessage = it.errorMessage
                        shippingRecommendationData.errorId = it.errorId
                        shippingRecommendationData
                    }
            }
            .flatMap { shippingRecommendationData ->
                getScheduleDeliveryUseCase.execute(scheduleDeliveryParam)
                    .map {
                        shippingRecommendationData.additionalDeliveryData = it.scheduleDeliveryData.additionalDeliveryData
                        shippingRecommendationData
                    }
            }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql?.unsubscribe()
    }
}
