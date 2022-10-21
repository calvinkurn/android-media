package com.tokopedia.logisticcart.scheduledelivery.model

import com.google.gson.annotations.SerializedName

data class ScheduleDeliveryResponse(
    @SerializedName("schedule_delivery_data")
    val scheduleDeliveryData: ScheduleDeliveryData = ScheduleDeliveryData()
)
