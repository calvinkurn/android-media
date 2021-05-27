package com.tokopedia.shop.score.detail_old.domain.model

import com.google.gson.annotations.SerializedName

data class ShopScoreResult(
    @SerializedName("shopScoreSummary")
    val shopScoreSummary: ShopScoreSummary? = null,
    @SerializedName("shopScoreDetail")
    val shopScoreDetail: List<ShopScoreDetail> = emptyList()
)