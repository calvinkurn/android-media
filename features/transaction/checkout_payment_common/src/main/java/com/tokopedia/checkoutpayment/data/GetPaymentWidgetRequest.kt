package com.tokopedia.checkoutpayment.data

import com.google.gson.annotations.SerializedName

data class GetPaymentWidgetRequest(
    @SerializedName("source")
    val source: String = "",
    @SerializedName("chosen_payment")
    val chosenPayment: GetPaymentWidgetChosenPaymentRequest = GetPaymentWidgetChosenPaymentRequest(),
    @SerializedName("cart_metadata")
    val cartMetadata: String = "",
    @SerializedName("client_metadata")
    val paymentRequest: String = ""
) {
    companion object {
        const val SOURCE_OCC = "one-click-checkout"
    }
}

data class GetPaymentWidgetChosenPaymentRequest(
    @SerializedName("gateway_code")
    val gatewayCode: String = "",
    @SerializedName("tenure_type")
    val tenureType: Int = 0,
    @SerializedName("option_id")
    val optionId: String = "",
    @SerializedName("metadata")
    val metadata: String = ""
)
