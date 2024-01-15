package com.tokopedia.content.product.preview.viewmodel.action

/**
 * @author by astidhiyaa on 12/12/23
 */
sealed interface ProductPreviewUiAction {

    object InitializeProductMainData : ProductPreviewUiAction
    data class InitializeReviewMainData(val page: Int) : ProductPreviewUiAction

    data class ProductSelected(val position: Int) : ProductPreviewUiAction
}
