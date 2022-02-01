package com.tokopedia.vouchercreation.product.list.view.model

data class ProductUiModel(
        var isSelectAll: Boolean = false,
        var isSelected: Boolean = false,
        var imageUrl: String = "",
        var id: String = "",
        var productName: String = "",
        var sku: String = "",
        var price: String = "",
        var soldNStock: String = "",
        var hasVariant: Boolean = false,
        var variants: List<ProductVariant> = listOf(),
        var isVariantsEmpty: Boolean = variants.isEmpty()
)