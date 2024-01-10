package com.tokopedia.content.product.preview.view.uimodel.product

import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel

data class ProductContentUiModel(
    val productId: String = "",
    val content: List<ContentUiModel> = emptyList(),
    val indicator: List<ProductIndicatorUiModel> = emptyList()
)
