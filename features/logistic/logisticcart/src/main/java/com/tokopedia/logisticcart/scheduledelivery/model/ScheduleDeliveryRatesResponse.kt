package com.tokopedia.logisticcart.scheduledelivery.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.ScheduleDeliveryData

data class ScheduleDeliveryRatesResponse(
    @SerializedName("data")
    val scheduleDeliveryData: ScheduleDeliveryData = ScheduleDeliveryData()
)
