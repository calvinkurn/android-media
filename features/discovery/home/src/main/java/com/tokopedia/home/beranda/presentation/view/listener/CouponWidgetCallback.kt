package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.CouponWidgetTracker
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.viewholders.coupon.CouponWidgetListener
import com.tokopedia.home_component.visitable.CouponCtaState
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.home_component.visitable.CouponWidgetDataModel
import com.tokopedia.track.TrackApp
import java.util.HashMap

class CouponWidgetCallback(val listener: HomeCategoryListener) : CouponWidgetListener {

    override fun ctaClick(data: CouponWidgetDataModel, state: CouponCtaState, position: Int) {
        when(state) {
            is CouponCtaState.Claim -> {
                if (state.data.catalogId.isEmpty()) return
                listener.onCouponWidgetClaim(data, state.data.catalogId, position)
            }
            is CouponCtaState.Redirect -> {
                val actionLink = state.data.appLink.ifEmpty { state.data.url }
                listener.onDynamicChannelClicked(actionLink)
            }
            is CouponCtaState.OutOfStock -> Unit
        }
    }

    override fun ctaClickTrack(data: CouponWidgetDataItemModel, position: Int) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            CouponWidgetTracker.ctaClick(
                position = position,
                coupon = data,
                userId = listener.userId,
                model = data.trackerModel.trackingAttributionModel,
            ) as HashMap<String, Any>
        )
    }

    override fun impressionTrack(position: Int, coupons: List<CouponWidgetDataItemModel>) {
        listener.getTrackingQueueObj()?.putEETracking(
            CouponWidgetTracker.impress(
                position = position,
                coupons = coupons,
                userId = listener.userId
            ) as HashMap<String, Any>
        )
    }
}
