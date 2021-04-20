package com.tokopedia.shop.score.domain.model

import com.google.gson.annotations.SerializedName

data class ShopScoreDetail(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("value")
    val value: Float = 0f,
    @SerializedName("maxValue")
    val maxValue: Float = 0f,
    @SerializedName("color")
    val color: String? = null,
    @SerializedName("description")
    val description: String? = null
)