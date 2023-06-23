package com.tokopedia.tokopedianow.category.presentation.model

import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel

data class CategoryAtcTrackerModel(
    val index: Int = 0,
    val categoryIdL1: String = "",
    val warehouseId: String = "",
    val headerName: String = "",
    val quantity: Int = 0,
    val shopId: String = "",
    val shopName: String = "",
    val shopType: String = "",
    val categoryBreadcrumbs: String = "",
    val product: ProductCardCompactUiModel,
    val layoutType: String = ""
)
