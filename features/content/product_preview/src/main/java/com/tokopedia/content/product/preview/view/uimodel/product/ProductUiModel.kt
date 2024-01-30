package com.tokopedia.content.product.preview.view.uimodel.product

data class ProductUiModel(
    val productList: List<ProductContentUiModel>
) {
    companion object {
        val Empty
            get() = ProductUiModel(
                productList = emptyList()
            )
    }
}
