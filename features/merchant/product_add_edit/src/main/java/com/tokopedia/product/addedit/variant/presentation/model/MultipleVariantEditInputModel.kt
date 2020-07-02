package com.tokopedia.product.addedit.variant.presentation.model

import java.math.BigInteger

data class MultipleVariantEditInputModel(
        var price: String = "",
        var stock: BigInteger = 0.toBigInteger(),
        var sku: String = "",
        var selection: MutableList<MutableList<Int>> = mutableListOf()
)