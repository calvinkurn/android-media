package com.tokopedia.product.addedit.variant.presentation.model

data class MultipleVariantEditInputModel(
        var price: String = "",
        var stock: String = "",
        var sku: String = "",
        var weight: String = "",
        var selection: MutableList<MutableList<Int>> = mutableListOf()
)