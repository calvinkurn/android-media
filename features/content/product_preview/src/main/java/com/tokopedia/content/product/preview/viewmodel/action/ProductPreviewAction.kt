package com.tokopedia.content.product.preview.viewmodel.action

import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewReportUiModel

/**
 * @author by astidhiyaa on 12/12/23
 */
sealed interface ProductPreviewAction {
    object CheckInitialSource : ProductPreviewAction
    object FetchMiniInfo : ProductPreviewAction
    object InitializeReviewMainData : ProductPreviewAction
    object ProductActionFromResult : ProductPreviewAction
    object LikeFromResult : ProductPreviewAction
    object ToggleReviewWatchMode : ProductPreviewAction
    object FetchReviewByIds : ProductPreviewAction
    data class ProductMediaSelected(val position: Int) : ProductPreviewAction
    data class ReviewContentSelected(val position: Int) : ProductPreviewAction
    data class ReviewContentScrolling(val position: Int, val isScrolling: Boolean) : ProductPreviewAction
    data class ReviewMediaSelected(val position: Int) : ProductPreviewAction
    data class TabSelected(val position: Int) : ProductPreviewAction
    data class FetchReview(val isRefresh: Boolean, val page: Int) : ProductPreviewAction
    data class ProductAction(val model: BottomNavUiModel) : ProductPreviewAction
    data class Navigate(val appLink: String) : ProductPreviewAction
    data class SubmitReport(val model: ReviewReportUiModel) : ProductPreviewAction
    data class ClickMenu(val isFromLogin: Boolean) : ProductPreviewAction
    data class Like(val item: ReviewLikeUiState) : ProductPreviewAction
}
