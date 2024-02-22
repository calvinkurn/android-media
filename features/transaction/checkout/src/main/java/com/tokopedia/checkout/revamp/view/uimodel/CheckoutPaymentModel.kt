package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.checkoutpayment.data.GetPaymentWidgetResponse
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetData

data class CheckoutPaymentModel(
    override val cartStringGroup: String = "",
    val widget: CheckoutPaymentWidgetData,
    val metadata: String = "",
    val enable: Boolean = false,
    val defaultErrorMessage: String = "",
    val data: GetPaymentWidgetResponse? = null
) : CheckoutItem
