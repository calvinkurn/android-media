package com.tokopedia.home.analytics.v2

import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.visitable.CouponCtaState
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object CouponWidgetTracker : BaseTrackerConst() {

    private const val EVENT_IMPRESSION_ACTION = "impression on banner coupon widget"
    private const val EVENT_CTA_CLICK_ACTION = "click on CTA button coupon widget"

    fun impress(
        position: Int,
        userId: String,
        coupons: List<CouponWidgetDataItemModel>
    ): Map<String, Any?> {
        // since the value is same, let's get for the first position of array the data is similar due got mapped from [CouponWidgetMapper].
        // the trackingAttributionModel extraction due to avoid send the large of [ChannelModel] object.
        val trackModel = coupons.first().trackerModel
        val trackAttributionModel = coupons.first().trackerModel.trackingAttributionModel

        val trackingBuilder = BaseTrackerBuilder().constructBasicPromotionView(
            event = Event.VIEW_ITEM,
            eventAction = EVENT_IMPRESSION_ACTION,
            eventCategory = Category.HOMEPAGE,
            eventLabel = "${trackAttributionModel.channelId} - ${trackAttributionModel.headerName}",
            promotions = listOf(
                Promotion(
                    id = "${trackAttributionModel.channelId}_${trackAttributionModel.bannerId}_${trackAttributionModel.categoryId}_${trackAttributionModel.persoType}",
                    name = "/ - p${position + 1} - coupon widget - banner - ${trackModel.gridId} - ${trackAttributionModel.headerName}",
                    position = (position + 1).toString(),
                    creative = trackModel.attribution
                )
            )
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(trackAttributionModel.channelId)
            .appendUserId(userId)

        return trackingBuilder.build()
    }

    fun ctaClick(
        position: Int,
        userId: String,
        model: TrackingAttributionModel,
        coupon: CouponWidgetDataItemModel
    ): Map<String, Any?> {
        val actionName = when (coupon.button) {
            is CouponCtaState.Claim -> "claim"
            is CouponCtaState.Redirect -> "redirect"
            else -> ""
        }

        val trackingBuilder = BaseTrackerBuilder().constructBasicPromotionClick(
            event = Event.SELECT_CONTENT,
            eventAction = EVENT_CTA_CLICK_ACTION,
            eventCategory = Category.HOMEPAGE,
            eventLabel = "${model.channelId} - $actionName - ${model.headerName}",
            promotions = listOf(
                Promotion(
                    id = "${model.channelId}_${model.bannerId}_${model.categoryId}_${model.persoType}",
                    name = "/ - p${position + 1} - coupon widget - banner - ${coupon.trackerModel.gridId} - ${model.headerName}",
                    position = (position + 1).toString(),
                    creative = coupon.trackerModel.attribution
                )
            )
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(model.channelId)
            .appendCampaignCode(coupon.trackerModel.trackingAttributionModel.campaignCode)
            .appendUserId(userId)

        return trackingBuilder.build()
    }
}
