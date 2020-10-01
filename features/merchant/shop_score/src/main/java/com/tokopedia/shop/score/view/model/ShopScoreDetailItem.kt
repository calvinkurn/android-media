package com.tokopedia.shop.score.view.model

data class ShopScoreDetailItem(
    var title: String?,
    val value: Int,
    val maxValue: Int,
    val description: String?,
    val progressBarColor: String?
)