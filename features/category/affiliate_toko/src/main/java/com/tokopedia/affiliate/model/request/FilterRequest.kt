package com.tokopedia.affiliate.model.request

import com.google.gson.annotations.SerializedName

data class FilterRequest(
    @SerializedName("DayRange")
    val DayRange: Int,
    @SerializedName("Page")
    val Page: Int,
    @SerializedName("Limit")
    val Limit: Int
)
