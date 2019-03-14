package com.tokopedia.promocheckout.common.util

import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView

val RED_STATE = "red"
val GREY = "grey"
val GREEN = "green"

val EXTRA_PROMO_DATA = "EXTRA_PROMO_DATA"

val MERCHANT = "merchant_voucher"

fun String?.mapToStatePromoCheckout() : TickerCheckoutView.State{
    when(this){
        RED_STATE -> return TickerCheckoutView.State.FAILED
        GREY -> return TickerCheckoutView.State.INACTIVE
        GREEN -> return TickerCheckoutView.State.ACTIVE
        else -> return TickerCheckoutView.State.EMPTY
    }
}

fun String?.mapToStatePromoStackingCheckout() : TickerPromoStackingCheckoutView.State{
    when(this){
        RED_STATE -> return TickerPromoStackingCheckoutView.State.FAILED
        GREY -> return TickerPromoStackingCheckoutView.State.INACTIVE
        GREEN -> return TickerPromoStackingCheckoutView.State.ACTIVE
        else -> return TickerPromoStackingCheckoutView.State.EMPTY
    }
}

fun String?.mapToVariantPromoStackingCheckout() : TickerPromoStackingCheckoutView.Variant{
    when(this){
        MERCHANT -> return TickerPromoStackingCheckoutView.Variant.MERCHANT
        else -> return TickerPromoStackingCheckoutView.Variant.GLOBAL
    }
}