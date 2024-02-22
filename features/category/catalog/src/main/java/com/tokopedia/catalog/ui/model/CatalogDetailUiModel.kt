package com.tokopedia.catalog.ui.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalog.R

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
    val textColor: Int = 0,
    val isVisible: Boolean = false
)

data class PriceCtaSellerOfferingProperties(
    val catalogId: String = "",
    val productId: String = "",
    val shopId: String = "",
    val warehouseId: String = "",
    val shopName: String = "",
    val price: String = "",
    val slashPrice: String = "",
    val shopRating: String = "",
    val sold: String = "0",
    val badge: String = "",
    val bgColor: Int = 0,
    val colorBorderButton: Int = R.color.catalog_dms_light_color,
    val textColorPrice: Int = R.color.catalog_dms_light_color,
    val isDarkTheme: Boolean = false,
    val isVisible: Boolean = false,
    val isVariant: Boolean = false
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
