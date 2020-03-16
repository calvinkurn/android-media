package com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout

import com.google.gson.annotations.SerializedName

data class CheckoutOccGqlResponse(
        @SerializedName("one_click_checkout")
        val response: CheckoutOccResponse
)

data class CheckoutOccResponse(
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: Data = Data()
)

data class Header(
        @SerializedName("messages")
        val messages: List<String> = emptyList(),
        @SerializedName("reason")
        val reason: String = "",
        @SerializedName("error_code")
        val errorCode: String = ""
)

data class Data(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("error")
        val error: Error = Error(),
        @SerializedName("payment_parameter")
        val paymentParameter: PaymentParameter = PaymentParameter()
)

data class Error(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("reason")
        val reason: String = "",
        @SerializedName("error_code")
        val errorCode: String = ""
)

data class PaymentParameter(
        @SerializedName("callback_url")
        val callbackUrl: String = "",
        @SerializedName("payload")
        val payload: String = "",
        @SerializedName("redirect_param")
        val redirectParam: String = ""
)