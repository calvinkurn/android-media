package com.tokopedia.checkoutpayment.data

import com.google.gson.annotations.SerializedName

data class GetPaymentWidgetRequest(
    @SerializedName("source")
    val source: String = "",
    @SerializedName("gateway_code")
    val gatewayCode: String = "",
    @SerializedName("tenure_type")
    val tenureType: Int = 0,
    @SerializedName("payment_metadata")
    val paymentMetadata: String = "",
    @SerializedName("detail_data")
    val paymentRequest: PaymentRequest
)
