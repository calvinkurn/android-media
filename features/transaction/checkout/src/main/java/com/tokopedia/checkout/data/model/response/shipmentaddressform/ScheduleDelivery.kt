package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

/**
 * Created by victor on 13/10/22
 */

data class ScheduleDelivery(
    @SerializedName("timeslot_id")
    val timeslotId: Long = 0L,
    @SerializedName("schedule_date")
    val scheduleDate: String = "",
    @SerializedName("validation_metadata")
    val validationMetadata: String = "",
)
