package com.tokopedia.content.product.preview.viewmodel.state

import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.content.product.preview.view.uimodel.product.ProductIndicatorUiModel

data class ProductPreviewUiState(
    val productContent: List<ContentUiModel> = emptyList(),
    val productIndicator: List<ProductIndicatorUiModel> = emptyList(),
    val reviewContent: List<ReviewUiModel> = emptyList()
)
