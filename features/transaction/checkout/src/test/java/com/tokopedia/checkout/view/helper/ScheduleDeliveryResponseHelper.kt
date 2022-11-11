package com.tokopedia.checkout.view.helper

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.ScheduleDeliveryData
import com.tokopedia.logisticcart.scheduledelivery.model.ScheduleDeliveryRatesResponse

object ScheduleDeliveryResponseHelper {

    fun getMockScheduleDeliveryRatesResponse(
        scheduleDeliveryRatesResponse: ScheduleDeliveryRatesResponse,
        available: Boolean? = null,
        hidden: Boolean? = null,
        recommend: Boolean? = null,
    ): ScheduleDeliveryRatesResponse {
        val scheduleDeliveryData = scheduleDeliveryRatesResponse.scheduleDeliveryData
        return ScheduleDeliveryRatesResponse(
            ScheduleDeliveryData(
                scheduleDeliveryData.ratesId,
                available ?: scheduleDeliveryData.available,
                hidden ?: scheduleDeliveryData.hidden,
                recommend ?: scheduleDeliveryData.recommend,
                scheduleDeliveryData.deliveryType,
                scheduleDeliveryData.title,
                scheduleDeliveryData.text,
                scheduleDeliveryData.notice,
                scheduleDeliveryData.ticker,
                scheduleDeliveryData.labels,
                scheduleDeliveryData.error,
                scheduleDeliveryData.deliveryServices
            )
        )
    }
}
