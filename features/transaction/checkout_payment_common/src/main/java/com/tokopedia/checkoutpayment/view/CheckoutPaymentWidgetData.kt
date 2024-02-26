package com.tokopedia.checkoutpayment.view

data class CheckoutPaymentWidgetData(
    val state: CheckoutPaymentWidgetState = CheckoutPaymentWidgetState.None,
) {

    val isValidStateToContinue: Boolean
        get() {
            return state != CheckoutPaymentWidgetState.Error
        }
}

sealed class CheckoutPaymentWidgetState {
    object None : CheckoutPaymentWidgetState()
    object Loading : CheckoutPaymentWidgetState()
    object Error : CheckoutPaymentWidgetState()
    object Normal : CheckoutPaymentWidgetState()
}
