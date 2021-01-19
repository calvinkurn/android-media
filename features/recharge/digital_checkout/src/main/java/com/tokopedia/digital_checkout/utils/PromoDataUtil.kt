package com.tokopedia.digital_checkout.utils

import com.tokopedia.promocheckout.common.util.GREEN
import com.tokopedia.promocheckout.common.util.GREY
import com.tokopedia.promocheckout.common.util.RED_STATE
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView

/**
 * @author by jessica on 13/01/21
 */

object PromoDataUtil {
    fun TickerCheckoutView.State.mapToStatePromoCheckout(): TickerPromoStackingCheckoutView.State {
        when (this) {
            TickerCheckoutView.State.FAILED -> return TickerPromoStackingCheckoutView.State.FAILED
            TickerCheckoutView.State.INACTIVE -> return TickerPromoStackingCheckoutView.State.INACTIVE
            TickerCheckoutView.State.ACTIVE -> return TickerPromoStackingCheckoutView.State.ACTIVE
            else -> return TickerPromoStackingCheckoutView.State.EMPTY
        }
    }
}