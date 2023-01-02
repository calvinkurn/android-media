package com.tokopedia.oneclickcheckout.order.data.payment

import com.google.gson.annotations.SerializedName

class PaymentFeeRequest(
    @SerializedName("profile_code")
    val profileCode: String = "",
    @SerializedName("gateway_code")
    val gatewayCode: String = "",
    @SerializedName("transaction_amount")
    val transactionAmount: Double = 0.0,
)
