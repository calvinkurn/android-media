package com.tokopedia.catalog.ui.model

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO

data class CatalogProductAtcUiModel(
    val productId: String = "",
    val shopId: String = "",
    val quantity: Int = Int.ONE,
    val isVariant: Boolean = false,
    val warehouseId: String = Int.ZERO.toString()
)
