package com.tokopedia.shop.common.graphql.data.shopscore

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Result(
        @SerializedName("badgeScore")
        @Expose
        val badgeScore: Int = 0,
        @SerializedName("shopID")
        @Expose
        val shopID: String = "",
        @SerializedName("shopScore")
        @Expose
        val shopScore: Int = 0,
        @SerializedName("shopScoreSummary")
        @Expose
        val shopScoreSummary: ShopScoreSummary = ShopScoreSummary()
)