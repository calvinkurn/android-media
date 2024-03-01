package com.tokopedia.home_component.viewholders.coupon

import com.tokopedia.discovery_component.widgets.automatecoupon.ButtonState
import com.tokopedia.home_component.visitable.CouponCtaState
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.home_component.visitable.CouponWidgetDataModel

object ButtonStateHandler {

    operator fun invoke(
        oldWidgetData: CouponWidgetDataModel,
        data: CouponWidgetDataItemModel,
        position: Int,
        listener: CouponWidgetListener
    ): ButtonState {
        return when (data.button) {
            is CouponCtaState.Claim -> ButtonState.Claim {
                listener.ctaClick(oldWidgetData, data.button, position)
                listener.ctaClickTrack(data, position)
            }
            is CouponCtaState.Redirect -> ButtonState.Redirection {
                listener.ctaClick(oldWidgetData, data.button, position)
                listener.ctaClickTrack(data, position)
            }
            is CouponCtaState.OutOfStock -> ButtonState.OutOfStock
        }
    }
}
