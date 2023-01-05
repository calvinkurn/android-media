package com.tokopedia.logisticcart.scheduledelivery.domain.usecase

import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.scheduledelivery.domain.mapper.ScheduleDeliveryMapper
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import rx.Observable
import javax.inject.Inject

class GetRatesWithScheduleUseCase @Inject constructor(
    private val getRatesUseCase: GetRatesUseCase,
    private val getScheduleDeliveryUseCase: GetScheduleDeliveryUseCase,
    private val scheduler: SchedulerProvider,
    private val mapper: ScheduleDeliveryMapper,
) {

    fun execute(ratesParam: RatesParam, warehouseId: String): Observable<ShippingRecommendationData> {
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
                getScheduleDeliveryUseCase.execute(mapper.map(ratesParam, warehouseId))
                    .map {
                        shippingRecommendationData.scheduleDeliveryData = it.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
                        shippingRecommendationData
                    }
            }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
    }
}
