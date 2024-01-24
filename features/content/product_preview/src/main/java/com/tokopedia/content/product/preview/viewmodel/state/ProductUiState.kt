package com.tokopedia.content.product.preview.viewmodel.state

import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.PageState
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.content.product.preview.view.uimodel.product.IndicatorUiModel

data class ProductUiState(
    val productContent: List<ContentUiModel> = emptyList(),
    val productIndicator: List<IndicatorUiModel> = emptyList()
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
