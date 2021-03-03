package com.tokopedia.shop.score.performance.presentation.model

data class ShopInfoLevelUiModel (var shopIncome: String = "",
                                 var productSold: String = "",
                                 var cardTooltipLevelList: List<CardTooltipLevelUiModel> = listOf())