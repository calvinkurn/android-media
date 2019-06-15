package com.tokopedia.shop.common.graphql.data.shopscore

import com.google.gson.annotations.SerializedName

data class ShopScoreSummary(
        @SerializedName("color")
        val color: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("maxValue")
        val maxValue: Int = 0,
        @SerializedName("title")
        val title: String = "",
        @SerializedName("value")
        val value: Int = 0
)