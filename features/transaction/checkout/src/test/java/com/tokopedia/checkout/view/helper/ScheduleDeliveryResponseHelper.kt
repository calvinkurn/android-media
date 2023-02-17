package com.tokopedia.checkout.view.helper

import com.tokopedia.logisticcart.scheduledelivery.domain.entity.response.OngkirGetScheduledDeliveryRates
import com.tokopedia.logisticcart.scheduledelivery.domain.entity.response.ScheduleDeliveryRatesResponse
import com.tokopedia.logisticcart.scheduledelivery.domain.model.ScheduleDeliveryData

object ScheduleDeliveryResponseHelper {

    fun getMockScheduleDeliveryRatesResponse(
        scheduleDeliveryRatesResponse: ScheduleDeliveryRatesResponse,
        available: Boolean? = null,
        hidden: Boolean? = null,
        recommend: Boolean? = null
    ): ScheduleDeliveryRatesResponse {
        val scheduleDeliveryData = scheduleDeliveryRatesResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
        return ScheduleDeliveryRatesResponse(
            OngkirGetScheduledDeliveryRates(
                ScheduleDeliveryData(
                    scheduleDeliveryData.ratesId,
                    available ?: scheduleDeliveryData.available,
                    hidden ?: scheduleDeliveryData.hidden,
                    recommend ?: scheduleDeliveryData.recommend,
                    scheduleDeliveryData.deliveryType,
                    scheduleDeliveryData.title,
                    scheduleDeliveryData.text,
                    scheduleDeliveryData.notice,
                    scheduleDeliveryData.error,
                    scheduleDeliveryData.deliveryServices
                )
            )
        )
    }
}
