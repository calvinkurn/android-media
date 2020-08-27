package com.tokopedia.oneclickcheckout.order.view.model

data class CheckoutOccData(
        val status: String,
        val headerMessage: String?,
        val result: CheckoutOccResult
)

data class CheckoutOccResult(
        val success: Int,
        val error: CheckoutOccErrorData,
        val paymentParameter: CheckoutOccPaymentParameter,
        val prompt: OccPrompt
)

data class CheckoutOccPaymentParameter(
        val callbackUrl: String,
        val payload: String,
        val redirectParam: CheckoutOccRedirectParam
)

data class CheckoutOccRedirectParam(
        val url: String,
        val gateway: String,
        val method: String,
        val form: String
)

data class CheckoutOccErrorData(
        val code: String = "",
        val imageUrl: String = "",
        val message: String = ""
)