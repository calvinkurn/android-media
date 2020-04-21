package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

data class OrderCost(
        val totalPrice: Double = 0.0,
        val totalItemPrice: Double = 0.0,
        val shippingFee: Double = 0.0,
        val insuranceFee: Double = 0.0,
        val paymentFee: Double = 0.0,
        val shippingDiscountAmount: Int = 0,
        val productDiscountAmount: Int = 0,
        val cashbacks: List<Pair<String, String>> = emptyList()
)