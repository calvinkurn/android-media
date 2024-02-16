package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.CouponWidgetTracker
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.viewholders.coupon.CouponWidgetListener
import com.tokopedia.home_component.visitable.CouponCtaState
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.home_component.visitable.CouponWidgetDataModel
import java.util.HashMap

class CouponWidgetCallback(val listener: HomeCategoryListener) : CouponWidgetListener {

    override fun ctaClick(oldWidgetData: CouponWidgetDataModel, state: CouponCtaState, position: Int) {
        when(state) {
            is CouponCtaState.Claim -> {
                if (state.data.catalogId.isEmpty()) return
                listener.onCouponWidgetClaim(oldWidgetData, state.data.catalogId, position)
            }
            is CouponCtaState.Redirect -> {
                val actionLink = state.data.appLink.ifEmpty { state.data.url }
                listener.onDynamicChannelClicked(actionLink)
            }
            is CouponCtaState.OutOfStock -> Unit
        }
    }

    override fun ctaClickTrack(state: CouponCtaState, position: Int) {
//        listener.sendEETracking(
//            CouponWidgetTracker.ctaClick(
//                userId = listener.userId,
//                channelId = model.trackingAttributionModel.channelId,
//                bannerId = model.trackingAttributionModel.bannerId,
//                headerName = model.trackingAttributionModel.headerName,
//                position = position
//            ) as HashMap<String, Any>
//        )
    }

    override fun impressionTrack(model: ChannelModel, position: Int, coupons: List<CouponWidgetDataItemModel>) {
        listener.sendEETracking(
            CouponWidgetTracker.impress(
                position = position,
                coupons = coupons,
                userId = listener.userId,
                model = model.trackingAttributionModel,
            ) as HashMap<String, Any>
        )
    }
}
