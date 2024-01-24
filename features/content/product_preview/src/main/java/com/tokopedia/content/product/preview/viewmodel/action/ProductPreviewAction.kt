package com.tokopedia.content.product.preview.viewmodel.action

import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewReportUiModel

/**
 * @author by astidhiyaa on 12/12/23
 */
sealed interface ProductPreviewAction {
    object InitializeProductMainData : ProductPreviewAction
    object FetchMiniInfo : ProductPreviewAction
    object ProductActionFromResult : ProductPreviewAction
    object LikeFromResult : ProductPreviewAction
    data class FetchReview(val isRefresh: Boolean) : ProductPreviewAction
    data class AddToChart(val model: BottomNavUiModel) : ProductPreviewAction
    data class Navigate(val appLink: String) : ProductPreviewAction
    data class SubmitReport(val model: ReviewReportUiModel) : ProductPreviewAction
    data class ClickMenu(val isFromLogin: Boolean) : ProductPreviewAction
    data class UpdateReviewPosition(val index: Int) : ProductPreviewAction
    data class Like(val item: ReviewLikeUiState) : ProductPreviewAction
    data class ProductSelected(val position: Int) : ProductPreviewAction
}
