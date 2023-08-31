package com.tokopedia.checkout.revamp.view.uimodel

data class CheckoutButtonPaymentModel(
    override val cartStringGroup: String = "",
    val enable: Boolean = false,
    val totalPrice: String = "-",
    val totalPriceNum: Double = 0.0,
    val quantity: Int = 0,
    var loading: Boolean = false,
    val useInsurance: Boolean = false
) : CheckoutItem
