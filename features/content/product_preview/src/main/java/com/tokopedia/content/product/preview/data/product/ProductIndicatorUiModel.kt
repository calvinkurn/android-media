package com.tokopedia.content.product.preview.data.product

import com.tokopedia.content.product.preview.data.ContentUiModel

data class ProductIndicatorUiModel(
    val id: String = "",
    val selected: Boolean = false,
    val variantName: String = "",
    val content: ContentUiModel = ContentUiModel(),
)
