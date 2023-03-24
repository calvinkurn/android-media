package com.tokopedia.checkout.domain.model.platformfee

class PlatformFeeRequest(
    var profileCode: String = "",
    var gatewayCode: String = "",
    var transactionAmount: Double = 0.0
)
