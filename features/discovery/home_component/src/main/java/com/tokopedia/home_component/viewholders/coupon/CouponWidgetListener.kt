package com.tokopedia.home_component.viewholders.coupon

import com.tokopedia.home_component.visitable.CouponCtaState

interface CouponWidgetListener {

    fun ctaClick(state: CouponCtaState, position: Int)
}
