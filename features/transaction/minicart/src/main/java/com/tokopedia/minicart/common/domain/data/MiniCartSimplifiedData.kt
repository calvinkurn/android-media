package com.tokopedia.minicart.common.domain.data

data class MiniCartSimplifiedData(
        var miniCartWidgetData: MiniCartWidgetData = MiniCartWidgetData(),
        var miniCartItems: List<MiniCartItem> = emptyList()
)

data class MiniCartWidgetData(
        var totalProductCount: Int = 0,
        var totalProductError: Int = 0,
        var totalProductPrice: Long = 0,
)

data class MiniCartItem(
        var isError: Boolean = false,
        var cartId: String = "",
        var productId: String = "",
        var parentProductId: String = "",
        var quantity: Int = 0
)