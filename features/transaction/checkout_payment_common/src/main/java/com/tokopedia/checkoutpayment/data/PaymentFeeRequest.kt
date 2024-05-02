package com.tokopedia.checkoutpayment.data

class PaymentFeeRequest(
    val profileCode: String = "",
    val gatewayCode: String = "",
    val transactionAmount: Double = 0.0,
    val additionalData: String = "",
    val paymentRequest: PaymentRequest
)
