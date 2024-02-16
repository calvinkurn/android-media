package com.tokopedia.content.product.preview.view.uimodel.product

data class ProductUiModel(
    val productMedia: List<ProductMediaUiModel>
) {
    companion object {
        val Empty
            get() = ProductUiModel(
                productMedia = emptyList()
            )
    }
}
