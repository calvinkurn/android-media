package com.tokopedia.digital_checkout.utils

import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView

/**
 * @author by jessica on 13/01/21
 */

object PromoDataUtil {
    fun TickerCheckoutView.State.mapToStatePromoCheckout(): ButtonPromoCheckoutView.State {
        when (this) {
            TickerCheckoutView.State.FAILED -> return ButtonPromoCheckoutView.State.INACTIVE
            TickerCheckoutView.State.INACTIVE -> return ButtonPromoCheckoutView.State.INACTIVE
            TickerCheckoutView.State.ACTIVE -> return ButtonPromoCheckoutView.State.ACTIVE
            else -> return ButtonPromoCheckoutView.State.ACTIVE
        }
    }
}