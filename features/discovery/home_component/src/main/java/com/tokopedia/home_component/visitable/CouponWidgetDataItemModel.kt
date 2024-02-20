package com.tokopedia.home_component.visitable

import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.home_component.model.TrackingAttributionModel

data class CouponWidgetDataItemModel(
    val trackerModel: CouponTrackerModel,
    val coupon: AutomateCouponModel,
    var button: CouponCtaState
)

data class CouponTrackerModel(
    val gridId: String = "",
    val attribution: String = "",
    val trackingAttributionModel: TrackingAttributionModel = TrackingAttributionModel()
)

sealed class CouponCtaState(val model: Data?) {
    data class Data(val catalogId: String, val url: String, val appLink: String)

    data class Claim(val data: Data) : CouponCtaState(data)
    data class Redirect(val data: Data) : CouponCtaState(data)
    object OutOfStock : CouponCtaState(null)
}
