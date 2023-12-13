package com.tokopedia.content.product.preview.viewmodel.state

import com.tokopedia.content.product.preview.data.ContentUiModel
import com.tokopedia.content.product.preview.data.product.ProductIndicatorUiModel

data class ProductPreviewUiState(
    val productContent: List<ContentUiModel> = emptyList(),
    val productIndicator: List<ProductIndicatorUiModel> = emptyList(),
)
