package com.tokopedia.checkoutpayment

data class CheckoutPaymentWidgetData(
    val state: CheckoutPaymentWidgetState = CheckoutPaymentWidgetState.None
)

sealed class CheckoutPaymentWidgetState {
    object None : CheckoutPaymentWidgetState()
    object Loading : CheckoutPaymentWidgetState()
    object Error : CheckoutPaymentWidgetState()
    object Normal : CheckoutPaymentWidgetState()
}
