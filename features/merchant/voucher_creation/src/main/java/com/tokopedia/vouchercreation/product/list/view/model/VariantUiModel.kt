package com.tokopedia.vouchercreation.product.list.view.model

data class VariantUiModel(
        var variantId: String = "",
        var variantName: String = "",
        var sku: String = "",
        var price: String = "",
        var priceTxt: String = "",
        var soldNStock: String = "",
        var isError: Boolean = false,
        var errorMessage: String = ""
)