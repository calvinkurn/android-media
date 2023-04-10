package com.tokopedia.oneclickcheckout.order.data.payment

class PaymentFeeRequest(
    val profileCode: String = "",
    val gatewayCode: String = "",
    val transactionAmount: Double = 0.0
)
