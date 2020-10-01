package com.tokopedia.shop.score.domain.model

import com.google.gson.annotations.SerializedName

data class ShopScoreResult(
    @SerializedName("shopID")
    val shopID: String,
    @SerializedName("shopScore")
    val shopScore: Int = 0,
    @SerializedName("shopScoreSummary")
    val shopScoreSummary: ShopScoreSummary? = null,
    @SerializedName("shopScoreDetail")
    val shopScoreDetail: List<ShopScoreDetail> = emptyList(),
    @SerializedName("badgeScore")
    val badgeScore: Int = 0
)