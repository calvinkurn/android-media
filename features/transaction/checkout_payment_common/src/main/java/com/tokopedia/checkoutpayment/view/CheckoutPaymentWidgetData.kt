package com.tokopedia.checkoutpayment.view

data class CheckoutPaymentWidgetData(
    val state: CheckoutPaymentWidgetState = CheckoutPaymentWidgetState.None,
    val metadata: String = "",
    val enable: Boolean = false,
    val defaultErrorMessage: String = ""
)

sealed class CheckoutPaymentWidgetState {
    object None : CheckoutPaymentWidgetState()
    object Loading : CheckoutPaymentWidgetState()
    object Error : CheckoutPaymentWidgetState()
    object Normal : CheckoutPaymentWidgetState()
}
