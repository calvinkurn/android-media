package com.tokopedia.oneclickcheckout.order.view.model

data class OrderTotal(
        val orderCost: OrderCost = OrderCost(),
        val buttonState: ButtonBayarState = ButtonBayarState.DISABLE,
        val isButtonChoosePayment: Boolean = false,
        val paymentErrorMessage: String? = null
)