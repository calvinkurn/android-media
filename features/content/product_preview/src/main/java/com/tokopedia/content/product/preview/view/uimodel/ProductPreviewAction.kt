package com.tokopedia.content.product.preview.view.uimodel

/**
 * @author by astidhiyaa on 12/12/23
 */
sealed class ProductPreviewAction {
    object FetchReview: ProductPreviewAction()
    object FetchMiniInfo: ProductPreviewAction()
    data class ProductAction(val model: BottomNavUiModel) : ProductPreviewAction()
    data class SubmitReport(val model: ReportUiModel) : ProductPreviewAction()
}
