package com.tokopedia.checkout.domain.model.platformfee

class PlatformFeeRequest(
    val profileCode: String = "",
    val gatewayCode: String = "",
    val transactionAmount: Double = 0.0
)
