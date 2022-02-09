package com.tokopedia.vouchercreation.product.list.view.model

data class ProductUiModel(
        var isSelectAll: Boolean = false,
        var isSelected: Boolean = false,
        var isError: Boolean = false,
        var errorMessage: String = "",
        var imageUrl: String = "",
        var id: String = "",
        var productName: String = "",
        var sku: String = "",
        var price: String = "",
        var sold: Int = 0,
        var soldNStock: String = "",
        var hasVariant: Boolean = false,
        var variants: List<VariantUiModel> = listOf()
)