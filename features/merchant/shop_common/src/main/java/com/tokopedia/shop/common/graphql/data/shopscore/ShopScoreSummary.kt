package com.tokopedia.shop.common.graphql.data.shopscore

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopScoreSummary(
        @SerializedName("color")
        @Expose
        val color: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("maxValue")
        @Expose
        val maxValue: Int = 0,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("value")
        @Expose
        val value: Int = 0
)