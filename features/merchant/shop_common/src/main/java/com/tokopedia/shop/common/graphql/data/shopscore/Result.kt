package com.tokopedia.shop.common.graphql.data.shopscore

import com.google.gson.annotations.SerializedName

data class Result(
        @SerializedName("badgeScore")
        val badgeScore: Int = 0,
        @SerializedName("shopID")
        val shopID: String = "",
        @SerializedName("shopScore")
        val shopScore: Int = 0,
        @SerializedName("shopScoreSummary")
        val shopScoreSummary: ShopScoreSummary = ShopScoreSummary()
)