package com.tokopedia.checkoutpayment.view

data class OrderPaymentFee(
    val code: String = "",
    val title: String = "",
    val fee: Double = 0.0,
    val showTooltip: Boolean = false,
    val showSlashed: Boolean = false,
    val slashedFee: Int = 0,
    val tooltipInfo: String = ""
)
