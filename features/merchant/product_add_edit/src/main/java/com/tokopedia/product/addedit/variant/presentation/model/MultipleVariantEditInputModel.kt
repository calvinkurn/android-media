package com.tokopedia.product.addedit.variant.presentation.model

import java.math.BigInteger

data class MultipleVariantEditInputModel(
        var price: BigInteger,
        var stock: Int,
        var sku: String,
        var selection: List<HashMap<Int, Boolean>>
)