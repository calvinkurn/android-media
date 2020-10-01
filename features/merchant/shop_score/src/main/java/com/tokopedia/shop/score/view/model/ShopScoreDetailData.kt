package com.tokopedia.shop.score.view.model

data class ShopScoreDetailData(
    val shopType: ShopType,
    val summary: ShopScoreDetailSummary?,
    val items: List<ShopScoreDetailItem>
)