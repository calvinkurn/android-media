package com.tokopedia.checkoutpayment.view

data class CheckoutPaymentWidgetData(
    val state: CheckoutPaymentWidgetState = CheckoutPaymentWidgetState.None,
    val errorMessage: String = ""
) {

    val isValidStateToContinue: Boolean
        get() {
            return state != CheckoutPaymentWidgetState.Error
        }

    val isValidStateToCheckout: Boolean
        get() {
            return state == CheckoutPaymentWidgetState.None || state == CheckoutPaymentWidgetState.Normal
        }
}

sealed class CheckoutPaymentWidgetState {
    object None : CheckoutPaymentWidgetState()
    object Loading : CheckoutPaymentWidgetState()
    object Error : CheckoutPaymentWidgetState()
    object Normal : CheckoutPaymentWidgetState()
}
