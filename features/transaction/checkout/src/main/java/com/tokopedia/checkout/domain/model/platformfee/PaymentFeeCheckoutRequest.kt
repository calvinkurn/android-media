package com.tokopedia.checkout.domain.model.platformfee

class PaymentFeeCheckoutRequest(
    var profileCode: String = "",
    var gatewayCode: String = "",
    var paymentAmount: Double = 0.0,
    var additionalData: String = ""
)
