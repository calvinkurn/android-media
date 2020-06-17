package com.tokopedia.product.addedit.variant.presentation.model

data class VariantDetailInputLayoutModel(
        var unitValueLabel: String = "",
        var isActive: Boolean = true,
        var price: String = "",
        var isPriceError: Boolean = false,
        var priceFieldErrorMessage: String = "",
        var stock: String = "",
        var isStockError: Boolean = false,
        var stockFieldErrorMessage: String = "",
        var sku: String = "",
        var isSkuFieldVisible: Boolean = true
)