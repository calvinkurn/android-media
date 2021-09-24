package com.tokopedia.shop.score.performance.presentation.model

data class PopupEndTenureUiModel(
    val shopType: ShopType = ShopType.REGULAR_MERCHANT,
    val shopScore: String = "",
    val shopLevel: String = ""
)