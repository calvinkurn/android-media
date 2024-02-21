package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon

import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponCtaState
import com.tokopedia.discovery_component.widgets.automatecoupon.ButtonState

object CtaActionHandler {

    operator fun invoke(state: AutomateCouponCtaState, listener: Listener?): ButtonState {
        return when (state) {
            is AutomateCouponCtaState.Claim -> {
                ButtonState.Custom(
                    text = state.properties.text,
                    action = { listener?.claim() }
                )
            }
            is AutomateCouponCtaState.Redirect -> {
                ButtonState.Custom(
                    text = state.properties.text,
                    action = { listener?.claim() }
                )
            }
            is AutomateCouponCtaState.OutOfStock -> ButtonState.OutOfStock
        }
    }

    interface Listener {
        fun claim()
        fun redirect(properties: AutomateCouponCtaState.Properties)
    }
}
