package com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout

import com.google.gson.annotations.SerializedName

data class CheckoutOccGqlResponse(
        @SerializedName("one_click_checkout")
        val response: CheckoutOccResponse
)

data class CheckoutOccResponse(
        @SerializedName("Header")
        val header: Header = Header(),
        @SerializedName("Status")
        val status: String = "",
        @SerializedName("Data")
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
        @SerializedName("Success")
        val success: Int = 0,
        @SerializedName("Error")
        val error: Error = Error(),
        @SerializedName("payment_parameter")
        val paymentParameter: PaymentParameter = PaymentParameter()
)

data class Error(
        @SerializedName("Code")
        val code: String = "",
        @SerializedName("ImageURL")
        val imageUrl: String = "",
        @SerializedName("Message")
        val message: String = ""
)

data class PaymentParameter(
        @SerializedName("CallbackURL")
        val callbackUrl: String = "",
        @SerializedName("Payload")
        val payload: String = "",
        @SerializedName("RedirectParam")
        val redirectParam: RedirectParam = RedirectParam()
)

data class RedirectParam(
        @SerializedName("URL")
        val url: String = "",
        @SerializedName("Gateway")
        val gateway: String = "",
        @SerializedName("Method")
        val method: String = "",
        @SerializedName("Form")
        val form: String = ""
)