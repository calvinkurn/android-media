package com.tokopedia.affiliate.model.request

import com.google.gson.annotations.SerializedName

data class FilterRequest(
    @SerializedName("DayRange")
    val dayRange: Int,
    @SerializedName("Page")
    val page: Int,
    @SerializedName("Limit")
    val limit: Int
)
