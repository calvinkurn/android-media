package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

data class OrderCart(
        var product: OrderProduct = OrderProduct(),
        var shop: OrderShop = OrderShop(),
        var kero: Kero = Kero(),
        var errors: List<String> = emptyList()
)