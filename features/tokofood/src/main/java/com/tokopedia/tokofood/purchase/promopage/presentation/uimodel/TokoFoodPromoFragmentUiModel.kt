package com.tokopedia.tokofood.purchase.promopage.presentation.uimodel

data class TokoFoodPromoFragmentUiModel(
        var tabs: List<TabUiModel> = emptyList(),
        var promoAmount: Long = 0L,
        var promoCount: Int = 0
)

data class TabUiModel(
        var id: String = "",
        var title: String = ""
)