package com.tokopedia.seller.menu.domain.query

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
        val calculateScore: Boolean = false,
        @Expose
        @SerializedName("getNextMinValue")
        val getNextMinValue: Boolean = true
)