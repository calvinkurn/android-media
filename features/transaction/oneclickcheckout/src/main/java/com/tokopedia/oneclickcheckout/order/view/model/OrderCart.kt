package com.tokopedia.oneclickcheckout.order.view.model

data class OrderCart(
        var product: OrderProduct = OrderProduct(),
        var shop: OrderShop = OrderShop(),
        var kero: Kero = Kero(),
        var errors: List<String> = emptyList()
)