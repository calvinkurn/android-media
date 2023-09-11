package com.tokopedia.catalog.ui.model

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class CatalogDetailUiModel(
    val widgets: List<Visitable<*>> = emptyList(),
    val navigationProperties: NavigationProperties,
    val priceCtaProperties: PriceCtaProperties,
    val productSortingStatus: Int
)

data class PriceCtaProperties (
    val price: String = "",
    val productName: String = "",
    val bgColor: Int = 0,
    val textColor: Int = 0
)

data class NavigationProperties (
    val isDarkMode: Boolean = false,
    val isPremium: Boolean = false,
    val bgColor: Int = 0,
    val title: String = ""
)
