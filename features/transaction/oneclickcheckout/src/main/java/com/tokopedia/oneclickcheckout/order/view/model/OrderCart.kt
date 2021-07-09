package com.tokopedia.oneclickcheckout.order.view.model

data class OrderCart(
        var cartString: String = "",
        var paymentProfile: String = "",
        var products: MutableList<OrderProduct> = ArrayList(),
        var shop: OrderShop = OrderShop(),
        var kero: OrderKero = OrderKero()
)