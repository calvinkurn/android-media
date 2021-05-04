package com.tokopedia.shop.score.performance.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopScoreLevelParam (
        @Expose
        @SerializedName("shopID")
        val shopID: String = "",
        @Expose
        @SerializedName("source")
        val source: String = "android",
        @Expose
        @SerializedName("calculateScore")
        val calculateScore: Boolean = true,
        @Expose
        @SerializedName("getNextMinValue")
        val getNextMinValue: Boolean = true
)