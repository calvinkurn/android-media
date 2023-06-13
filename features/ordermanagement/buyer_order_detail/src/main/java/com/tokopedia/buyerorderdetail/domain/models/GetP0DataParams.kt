package com.tokopedia.buyerorderdetail.domain.models

data class GetP0DataParams(
    val cart: String = "",
    val orderId: String = "0",
    val paymentId: String = "",
    val shouldCheckCache: Boolean
)
