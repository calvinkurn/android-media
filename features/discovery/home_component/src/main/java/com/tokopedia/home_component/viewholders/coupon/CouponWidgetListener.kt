package com.tokopedia.home_component.viewholders.coupon

import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.CouponCtaState
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel

interface CouponWidgetListener {

    // user action
    fun ctaClick(state: CouponCtaState, position: Int)

    // trackers
    fun ctaClickTrack(state: CouponCtaState, position: Int)
    fun impressionTrack(model: ChannelModel, position: Int, coupons: List<CouponWidgetDataItemModel>)
}
