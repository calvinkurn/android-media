package com.tokopedia.shop.score.detail_old.domain.model

import com.google.gson.annotations.SerializedName

data class ShopScoreSummary(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("value")
    val value: Float = 0f,
    @SerializedName("color")
    val color: String? = null
)