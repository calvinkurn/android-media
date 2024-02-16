package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetData

data class CheckoutPaymentModel(
    override val cartStringGroup: String = "",
    val widget: CheckoutPaymentWidgetData
) : CheckoutItem
