package com.tokopedia.logisticcart.scheduledelivery.domain.entity.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticcart.scheduledelivery.domain.model.ScheduleDeliveryData

data class ScheduleDeliveryRatesResponse(
    @SerializedName("ongkirGetScheduledDeliveryRates")
    val ongkirGetScheduledDeliveryRates: OngkirGetScheduledDeliveryRates = OngkirGetScheduledDeliveryRates()
)

data class OngkirGetScheduledDeliveryRates(
    @SerializedName("data")
    val scheduleDeliveryData: ScheduleDeliveryData = ScheduleDeliveryData()
)
