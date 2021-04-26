package com.tokopedia.oneclickcheckout.order.view.model

data class OrderCart(
        var cartId: String = "",
        var cartString: String = "",
        var paymentProfile: String = "",
        var product: OrderProduct = OrderProduct(),
        var shop: OrderShop = OrderShop(),
        var kero: OrderKero = OrderKero()
)