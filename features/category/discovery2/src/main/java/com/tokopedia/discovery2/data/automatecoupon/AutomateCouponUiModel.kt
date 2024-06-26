package com.tokopedia.discovery2.data.automatecoupon

import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel

data class AutomateCouponUiModel(
    val data: AutomateCouponModel,
    val ctaState: AutomateCouponCtaState,
    val redirectAppLink: String
)

sealed class AutomateCouponCtaState {
    data class Claim(
        val catalogId: Long,
        val properties: Properties = Properties.empty()
    ) : AutomateCouponCtaState()
    data class Redirect(val properties: Properties) : AutomateCouponCtaState()
    object OutOfStock : AutomateCouponCtaState()

    data class Properties(
        val text: String,
        val appLink: String,
        val url: String
    ) {

        companion object {
            fun empty() = Properties("", "", "")
        }
    }
}
