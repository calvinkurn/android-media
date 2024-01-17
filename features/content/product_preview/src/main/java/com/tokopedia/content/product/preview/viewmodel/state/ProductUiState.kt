package com.tokopedia.content.product.preview.viewmodel.state

import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.PageState
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.content.product.preview.view.uimodel.product.ProductIndicatorUiModel

data class ProductUiState(
    val productContent: List<ContentUiModel> = emptyList(),
    val productIndicator: List<ProductIndicatorUiModel> = emptyList(),
)

data class ReviewPageState(
    val reviewList: List<ReviewUiModel>,
    val pageState: PageState,
) {
    companion object {
        val Empty get() = ReviewPageState(
            reviewList = emptyList(),
            pageState = PageState.Unknown,
        )
    }
}
