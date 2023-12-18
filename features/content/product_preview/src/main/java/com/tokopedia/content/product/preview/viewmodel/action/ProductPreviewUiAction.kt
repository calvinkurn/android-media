package com.tokopedia.content.product.preview.viewmodel.action

import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel

sealed interface ProductPreviewUiAction {

    data class InitializeProductMainData(val data: ProductContentUiModel) : ProductPreviewUiAction

    data class ProductSelected(val position: Int) : ProductPreviewUiAction

}
