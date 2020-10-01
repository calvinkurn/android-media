package com.tokopedia.shop.score.domain.model

import com.google.gson.annotations.SerializedName

data class ShopScoreDetail(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("value")
    val value: Int = 0,
    @SerializedName("maxValue")
    val maxValue: Int = 0,
    @SerializedName("color")
    val color: String? = null,
    @SerializedName("description")
    val description: String? = null
)