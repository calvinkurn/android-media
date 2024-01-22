package com.tokopedia.content.product.preview.view.uimodel

/**
 * @author by astidhiyaa on 12/12/23
 */
sealed interface ProductPreviewAction {
    object InitializeProductMainData : ProductPreviewAction
    object FetchMiniInfo : ProductPreviewAction
    object ProductActionFromResult : ProductPreviewAction
    object LikeFromResult : ProductPreviewAction
    data class FetchReview(val isRefresh: Boolean) : ProductPreviewAction
    data class ProductAction(val model: BottomNavUiModel) : ProductPreviewAction
    data class Navigate(val appLink: String) : ProductPreviewAction
    data class SubmitReport(val model: ReportUiModel) : ProductPreviewAction
    data class ClickMenu(val isFromLogin: Boolean) : ProductPreviewAction
    data class UpdateReviewPosition(val index: Int) : ProductPreviewAction
    data class Like(val item: LikeUiState) : ProductPreviewAction
    data class ProductSelected(val position: Int) : ProductPreviewAction
}
