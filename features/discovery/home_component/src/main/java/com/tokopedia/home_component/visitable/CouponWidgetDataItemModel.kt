package com.tokopedia.home_component.visitable

import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel

data class CouponWidgetDataItemModel(
    val coupon: AutomateCouponModel,
    val button: CtaState
) {

    sealed class CtaState {
        object Claim : CtaState()
        object Redirect : CtaState()
        object OutOfStock : CtaState()
    }
}
