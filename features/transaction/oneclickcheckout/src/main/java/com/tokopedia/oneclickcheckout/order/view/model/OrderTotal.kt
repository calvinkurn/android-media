package com.tokopedia.oneclickcheckout.order.view.model

data class OrderTotal(
        val orderCost: OrderCost = OrderCost(),
        val buttonState: OccButtonState = OccButtonState.DISABLE,
        val buttonType: OccButtonType = OccButtonType.PAY,
        val showTickerError: Boolean = false
) {

    val isButtonChoosePayment: Boolean
        get() = buttonType == OccButtonType.CHOOSE_PAYMENT

    val isButtonPay: Boolean
        get() = buttonType == OccButtonType.PAY
}
