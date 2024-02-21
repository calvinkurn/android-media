package com.tokopedia.discovery2.data.automatecoupon

import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel

data class AutomateCouponUiModel(
    val data: AutomateCouponModel,
    val ctaState: AutomateCouponCtaState
)

sealed class AutomateCouponCtaState {
    data class Claim(val properties: Properties = Properties.empty()) : AutomateCouponCtaState()
    data class Redirect(val properties: Properties) : AutomateCouponCtaState()
    object OutOfStock : AutomateCouponCtaState()

    data class Properties(
        val isDisabled: Boolean,
        val text: String,
        val appLink: String,
        val url: String
    ) {

        companion object {
            fun empty() = Properties(false, "", "", "")
        }
    }
}
