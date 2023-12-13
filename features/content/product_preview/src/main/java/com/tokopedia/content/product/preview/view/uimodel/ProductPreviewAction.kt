package com.tokopedia.content.product.preview.view.uimodel

import com.tokopedia.content.common.report_content.model.ContentMenuItem

/**
 * @author by astidhiyaa on 12/12/23
 */
sealed interface ProductPreviewAction {
    object FetchReview : ProductPreviewAction
    object FetchMiniInfo : ProductPreviewAction
    data class ProductAction(val model: BottomNavUiModel) : ProductPreviewAction
    object AtcFromResult : ProductPreviewAction
    data class Navigate(val appLink: String) : ProductPreviewAction
    data class SubmitReport(val model: ReportUiModel) : ProductPreviewAction
    data class ClickMenu(val menus: List<ContentMenuItem>) : ProductPreviewAction
    data class Like(val state: LikeUiState) : ProductPreviewAction
}
