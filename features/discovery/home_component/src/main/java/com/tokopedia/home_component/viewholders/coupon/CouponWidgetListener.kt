package com.tokopedia.home_component.viewholders.coupon

import com.tokopedia.home_component.visitable.CouponCtaState
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.home_component.visitable.CouponWidgetDataModel

interface CouponWidgetListener {

    // user action
    fun ctaClick(data: CouponWidgetDataModel, state: CouponCtaState, position: Int)

    // trackers
    fun ctaClickTrack(data: CouponWidgetDataItemModel, position: Int)
    fun impressionTrack(position: Int, coupons: List<CouponWidgetDataItemModel>)
}
