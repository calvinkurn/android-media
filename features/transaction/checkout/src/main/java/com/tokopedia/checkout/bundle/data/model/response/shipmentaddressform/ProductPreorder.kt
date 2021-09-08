package com.tokopedia.checkout.bundle.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class ProductPreorder(
        @SerializedName("duration_text")
        val durationText: String = "",
        @SerializedName("duration_day")
        val durationDay: Int = 0
)