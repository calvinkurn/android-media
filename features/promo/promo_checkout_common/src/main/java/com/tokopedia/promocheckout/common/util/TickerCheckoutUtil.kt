package com.tokopedia.promocheckout.common.util

import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView

val RED_STATE = "red"
val GREY = "grey"
val GREEN = "green"

val EXTRA_PROMO_DATA = "EXTRA_PROMO_DATA"
val EXTRA_CLASHING_DATA = "EXTRA_CLASHING_DATA"
val EXTRA_TYPE = "EXTRA_TYPE"

val RESULT_CLASHING = 6374

val MERCHANT = "merchant"
val LOGISTIC = "logistic"
val GLOBAL = "global"

fun String?.mapToStatePromoCheckout() : TickerCheckoutView.State{
    when(this){
        RED_STATE -> return TickerCheckoutView.State.FAILED
        GREY -> return TickerCheckoutView.State.INACTIVE
        GREEN -> return TickerCheckoutView.State.ACTIVE
        else -> return TickerCheckoutView.State.EMPTY
    }
}

fun String?.mapToStatePromoStackingCheckout() : TickerPromoStackingCheckoutView.State{
    return when(this){
        RED_STATE -> TickerPromoStackingCheckoutView.State.FAILED
        GREY -> TickerPromoStackingCheckoutView.State.INACTIVE
        GREEN -> TickerPromoStackingCheckoutView.State.ACTIVE
        else -> TickerPromoStackingCheckoutView.State.EMPTY
    }
}

fun String?.mapToVariantPromoStackingCheckout() : TickerPromoStackingCheckoutView.Variant{
    return when(this){
        MERCHANT -> TickerPromoStackingCheckoutView.Variant.MERCHANT
        else -> TickerPromoStackingCheckoutView.Variant.GLOBAL
    }
}