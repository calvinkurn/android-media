package com.tokopedia.content.product.preview.viewmodel.action

sealed interface ProductPreviewUiAction {

    object InitializeMainData: ProductPreviewUiAction

    data class ProductSelected(val position: Int): ProductPreviewUiAction

}
