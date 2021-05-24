package com.tokopedia.shop.score.detail_old.view.model

data class ShopScoreDetailItem(
    var title: String?,
    val value: Float,
    val maxValue: Float,
    val description: String?,
    val progressBarColor: String?
)