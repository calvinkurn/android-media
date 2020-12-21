package com.tokopedia.homenav.mainnav.data.pojo.payment

data class PaymentX(
    val gatewayImg: String,
    val merchantCode: String,
    val paymentAmount: Int,
    val tickerMessage: String,
    val transactionID: String,
    val applink: String
)