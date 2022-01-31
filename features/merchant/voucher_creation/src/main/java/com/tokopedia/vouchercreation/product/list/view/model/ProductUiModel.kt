package com.tokopedia.vouchercreation.product.list.view.model

data class ProductUiModel(
        var isSelected: Boolean = false,
        var imageUrl: String = "",
        var id: String = "",
        var productName: String = "",
        var sku: String = "",
        var price: String = "",
        var soldNStock: String = "",
        var variants: List<ProductVariant> = listOf()
)