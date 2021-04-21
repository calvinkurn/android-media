package com.tokopedia.shop.score.penalty.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopScorePenaltySummaryParam(
        @SerializedName("startDate")
        @Expose
        val startDate: String = "",
        @SerializedName("endDate")
        @Expose
        val endDate: String = "",
        @SerializedName("source")
        @Expose
        val source: String = "android"
)