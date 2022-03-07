package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ProductPreorder(
        @SerializedName("duration_text")
        val durationText: String = "",
        @SerializedName("duration_day")
        val durationDay: Int = 0,
        @SerializedName("duration_unit_code")
        val durationUnitCode: Int = 0,
        @SerializedName("duration_unit_text")
        val durationUnitText: String = "",
        @SerializedName("duration_value")
        val durationValue: Int = 0
)