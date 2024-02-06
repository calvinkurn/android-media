package com.tokopedia.home_component.viewholders.coupon

import com.tokopedia.discovery_component.widgets.automatecoupon.ButtonState
import com.tokopedia.home_component.visitable.CouponCtaState

object ButtonStateHandler {

    operator fun invoke(
        state: CouponCtaState,
        position: Int,
        listener: CouponWidgetListener
    ): ButtonState {
        return when (state) {
            is CouponCtaState.Claim -> ButtonState.Claim { listener.ctaClick(state, position) }
            is CouponCtaState.Redirect -> ButtonState.Redirection { listener.ctaClick(state, position) }
            is CouponCtaState.OutOfStock -> ButtonState.OutOfStock
        }
    }
}
