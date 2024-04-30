package com.tokopedia.checkoutpayment.list.data

data class PaymentListingParamRequest(
    val merchantCode: String,
    val profileCode: String,
    val callbackUrl: String,
    val addressId: String,
    val version: String,
    val bid: String,
    val paymentRequest: String
)
