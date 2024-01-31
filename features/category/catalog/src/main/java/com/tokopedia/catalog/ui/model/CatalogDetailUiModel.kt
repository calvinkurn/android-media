package com.tokopedia.catalog.ui.model

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class CatalogDetailUiModel(
    val widgets: List<Visitable<*>> = emptyList(),
    val navigationProperties: NavigationProperties,
    val priceCtaProperties: PriceCtaProperties,
    val productSortingStatus: Int,
    val catalogUrl: String,
    val shareProperties: ShareProperties = ShareProperties(),
    val priceCtaSellerOfferingProperties: PriceCtaSellerOfferingProperties = PriceCtaSellerOfferingProperties()
)

data class PriceCtaProperties(
    val catalogId: String = "",
    val departmentId: String = "",
    val brand: String = "",
    val price: String = "",
    val productName: String = "",
    val bgColor: Int = 0,
    val textColor: Int = 0
)

data class PriceCtaSellerOfferingProperties(
    val catalogId: String = "",
    val productId: String = "",
    val shopName: String = "",
    val price: String = "",
    val slashPrice: String = "",
    val shopRating: String = "",
    val sold: String = "0",
    val badge: String = "",
    val textColor: Int = 0,
    val iconColor: Int = 0,
    val bgColor: Int = 0,
    val bgColorAtc: Int = 0,
    val isDarkTheme: Boolean = false
)

data class NavigationProperties(
    val isDarkMode: Boolean = false,
    val isPremium: Boolean = false,
    val bgColor: Int = 0,
    val title: String = ""
)

data class ShareProperties(
    val catalogId: String = "",
    val title: String = "",
    val images: List<String> = emptyList(),
    val catalogUrl: String = ""
)
