package com.tokopedia.catalog.ui.model

import com.tokopedia.kotlin.extensions.view.ONE

data class CatalogProductAtcUiModel (
    val productId: String = "",
    val shopId: String = "",
    val quantity: Int = Int.ONE,
    val isVariant: Boolean = false
)
