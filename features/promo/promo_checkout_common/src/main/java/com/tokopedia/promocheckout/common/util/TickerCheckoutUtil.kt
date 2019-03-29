package com.tokopedia.promocheckout.common.util

import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView

val RED_STATE = "red"
val GREY = "grey"
val GREEN = "green"

val EXTRA_PROMO_DATA = "EXTRA_PROMO_DATA"

fun String?.mapToStatePromoCheckout() : TickerCheckoutView.State{
    when(this){
        RED_STATE -> return TickerCheckoutView.State.FAILED
        GREY -> return TickerCheckoutView.State.INACTIVE
        GREEN -> return TickerCheckoutView.State.ACTIVE
        else -> return TickerCheckoutView.State.EMPTY
    }
}