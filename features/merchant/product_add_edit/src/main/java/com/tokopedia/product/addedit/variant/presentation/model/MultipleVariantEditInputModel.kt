package com.tokopedia.product.addedit.variant.presentation.model

import java.math.BigInteger

data class MultipleVariantEditInputModel(
        var price: BigInteger = 0.toBigInteger(),
        var stock: Int = 0,
        var sku: String = "",
        var selection: MutableList<MutableList<Int>> = mutableListOf()
)