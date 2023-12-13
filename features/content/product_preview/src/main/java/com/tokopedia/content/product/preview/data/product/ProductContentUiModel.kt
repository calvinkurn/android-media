package com.tokopedia.content.product.preview.data.product

import com.tokopedia.content.product.preview.data.ContentUiModel

data class ProductContentUiModel(
    val id: String = "",
    val content: List<ContentUiModel> = emptyList(),
    val indicators: List<ProductIndicatorUiModel> = emptyList(),
)
