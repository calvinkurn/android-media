package com.tokopedia.oneclickcheckout.order.view.model

data class CheckoutOccData(
        val status: String = "",
        val headerMessage: String? = null,
        val result: CheckoutOccResult = CheckoutOccResult()
)

data class CheckoutOccResult(
        val success: Int = 0,
        val error: CheckoutOccErrorData = CheckoutOccErrorData(),
        val paymentParameter: CheckoutOccPaymentParameter = CheckoutOccPaymentParameter(),
        val prompt: OccPrompt = OccPrompt()
)

data class CheckoutOccPaymentParameter(
        val callbackUrl: String = "",
        val payload: String = "",
        val redirectParam: CheckoutOccRedirectParam = CheckoutOccRedirectParam()
)

data class CheckoutOccRedirectParam(
        val url: String = "",
        val gateway: String = "",
        val method: String = "",
        val form: String = ""
)

data class CheckoutOccErrorData(
        val code: String = "",
        val imageUrl: String = "",
        val message: String = ""
)