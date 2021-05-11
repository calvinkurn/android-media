package com.tokopedia.minicart.common.domain.data

data class MiniCartSimplifiedData(
        var isError: Boolean = false,
        var cartId: String = "",
        var productId: String = "",
        var parentProductId: String = "",
        var quantity: Int = 0
)