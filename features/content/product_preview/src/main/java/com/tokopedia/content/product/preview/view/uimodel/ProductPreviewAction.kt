package com.tokopedia.content.product.preview.view.uimodel

/**
 * @author by astidhiyaa on 12/12/23
 */
sealed interface ProductPreviewAction {
    object FetchReview: ProductPreviewAction
    object FetchMiniInfo: ProductPreviewAction
    data class ProductAction(val model: BottomNavUiModel) : ProductPreviewAction
    object AtcFromResult: ProductPreviewAction
    data class Navigate(val appLink: String) : ProductPreviewAction
    data class SubmitReport(val model: ReportUiModel) : ProductPreviewAction
}
