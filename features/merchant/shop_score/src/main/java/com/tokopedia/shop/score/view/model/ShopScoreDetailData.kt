package com.tokopedia.shop.score.view.model

data class ShopScoreDetailData(
    val shopType: ShopType = ShopType.REGULAR_MERCHANT,
    val summary: ShopScoreDetailSummary? = null,
    val items: List<ShopScoreDetailItem> = emptyList()
)