package com.tokopedia.vouchercreation.product.list.view.model

data class ProductVariant(
        var variantId: String = "",
        var variantName: String = "",
        var sku: String = "",
        var price: String = "",
        var soldNStock: String = ""
)