package com.tokopedia.content.product.preview.view.uimodel

/**
 * @author by astidhiyaa on 12/12/23
 */
sealed interface ProductPreviewAction {
    object InitializeProductMainData : ProductPreviewAction
    object FetchReview : ProductPreviewAction
    object FetchMiniInfo : ProductPreviewAction
    object ProductActionFromResult : ProductPreviewAction
    data class ProductAction(val model: BottomNavUiModel) : ProductPreviewAction
    data class Navigate(val appLink: String) : ProductPreviewAction
    data class ProductSelected(val position: Int) : ProductPreviewAction
    data class SetProductVideoLastDuration(val duration: Long) : ProductPreviewAction
}
