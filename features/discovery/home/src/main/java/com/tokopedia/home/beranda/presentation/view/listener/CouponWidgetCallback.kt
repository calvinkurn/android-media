package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.viewholders.coupon.CouponWidgetListener
import com.tokopedia.home_component.visitable.CouponCtaState

class CouponWidgetCallback(val listener: HomeCategoryListener) : CouponWidgetListener {

    override fun ctaClick(state: CouponCtaState, position: Int) {
        when(state) {
            is CouponCtaState.Claim -> {
                if (state.data.catalogId.isEmpty()) return
                listener.onCouponWidgetClaim(state.data.catalogId, position)
            }
            is CouponCtaState.Redirect -> {
                val actionLink = state.data.appLink.ifEmpty { state.data.url }
                listener.onDynamicChannelClicked(actionLink)
            }
            is CouponCtaState.OutOfStock -> Unit
        }
    }
}
