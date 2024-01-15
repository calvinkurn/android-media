package com.tokopedia.content.product.preview.viewmodel.state

import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.product.ProductIndicatorUiModel

data class ProductUiState(
    val productContent: List<ContentUiModel> = emptyList(),
    val productIndicator: List<ProductIndicatorUiModel> = emptyList(),
)
