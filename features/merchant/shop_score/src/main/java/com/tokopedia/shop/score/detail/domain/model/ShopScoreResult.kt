package com.tokopedia.shop.score.detail.domain.model

import com.google.gson.annotations.SerializedName

data class ShopScoreResult(
    @SerializedName("shopScoreSummary")
    val shopScoreSummary: ShopScoreSummary? = null,
    @SerializedName("shopScoreDetail")
    val shopScoreDetail: List<ShopScoreDetail> = emptyList()
)