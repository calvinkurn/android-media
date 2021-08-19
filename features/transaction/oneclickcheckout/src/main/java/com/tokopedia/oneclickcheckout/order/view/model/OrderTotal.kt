package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.oneclickcheckout.common.data.model.OrderItem

data class OrderTotal(
        val orderCost: OrderCost = OrderCost(),
        val buttonState: OccButtonState = OccButtonState.DISABLE,
        val buttonType: OccButtonType = OccButtonType.PAY
): OrderItem {

    val isButtonChoosePayment: Boolean
        get() = buttonType == OccButtonType.CHOOSE_PAYMENT

    val isButtonPay: Boolean
        get() = buttonType == OccButtonType.PAY
}