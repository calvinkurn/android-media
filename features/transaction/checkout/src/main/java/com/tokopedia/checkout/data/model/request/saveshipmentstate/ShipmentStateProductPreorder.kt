package com.tokopedia.checkout.data.model.request.saveshipmentstate

import com.google.gson.annotations.SerializedName

data class ShipmentStateProductPreorder(
        @SerializedName("duration_day")
        var durationDay: Int = 0
)