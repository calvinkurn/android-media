package com.tokopedia.content.product.preview.view.uimodel.product

import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel

data class ProductIndicatorUiModel(
    val indicatorId: String = "",
    val selected: Boolean = false,
    val variantName: String = "",
    val content: ContentUiModel = ContentUiModel()
)
