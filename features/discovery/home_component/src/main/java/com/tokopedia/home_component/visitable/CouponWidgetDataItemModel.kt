package com.tokopedia.home_component.visitable

import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel

data class CouponWidgetDataItemModel(
    val coupon: AutomateCouponModel,
    var button: CouponCtaState
)

sealed class CouponCtaState(val model: Data?) {
    data class Data(val catalogId: String, val url: String, val appLink: String)

    data class Claim(val data: Data) : CouponCtaState(data)
    data class Redirect(val data: Data) : CouponCtaState(data)
    object OutOfStock : CouponCtaState(null)
}
