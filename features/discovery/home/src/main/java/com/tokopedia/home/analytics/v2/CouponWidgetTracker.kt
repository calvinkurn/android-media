package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.HomePageTracking.PROMOTIONS
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.visitable.CouponCtaState
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.track.constant.TrackerConstant

object CouponWidgetTracker : BaseTracking() {

    private const val IMPRESSION_TRACKER_ID = 49608
    private const val EVENT_IMPRESSION_ACTION = "impression on banner coupon widget"
    fun impress(
        position: Int,
        userId: String,
        coupons: List<CouponWidgetDataItemModel>
    ): Map<String, Any?> {
        // since the value is same, let's get for the first position of array the data is similar due got mapped from [CouponWidgetMapper].
        // the trackingAttributionModel extraction due to avoid send the large of [ChannelModel] object.
        val model = coupons.first().trackerModel.trackingAttributionModel

        return DataLayer.mapOf(
            Event.KEY, "view_item",
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, EVENT_IMPRESSION_ACTION,
            Label.KEY, "${model.channelId} - ${model.headerName}",
            TrackerConstant.TRACKER_ID, IMPRESSION_TRACKER_ID,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            ChannelId.KEY, model.channelId,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            TrackerConstant.USERID, userId,
            Ecommerce.KEY, DataLayer.mapOf(
                PROMOTIONS, coupons.mapIndexed { index, coupon ->
                    DataLayer.mapOf(
                        "creative_name", coupon.trackerModel.attribution,
                        "creative_slot", (position + 1).toString(),
                        "id", "${model.channelId}_${model.bannerId}_${model.categoryId}_${model.persoType}",
                        "name", "/ - p${index} - coupon widget - banner - ${coupon.trackerModel.gridId} - ${model.headerName}",
                    )
                }
            )
        )
    }

    private const val CTA_TRACKER_ID = 49609
    private const val EVENT_CTA_CLICK_ACTION = "click on CTA button coupon widget"
    fun ctaClick(
        position: Int,
        userId: String,
        model: TrackingAttributionModel,
        coupon: CouponWidgetDataItemModel
    ): Map<String, Any?> {
        val actionName = when (coupon.button) {
            is CouponCtaState.Claim -> "claim coupon"
            is CouponCtaState.Redirect -> "mulai belanja"
            else -> ""
        }

        return DataLayer.mapOf(
            Event.KEY, "select_content",
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, EVENT_CTA_CLICK_ACTION,
            Label.KEY, "${model.channelId} - $actionName - ${model.headerName}",
            TrackerConstant.TRACKER_ID, CTA_TRACKER_ID,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            ChannelId.KEY, model.channelId,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            TrackerConstant.USERID, userId,
            Ecommerce.KEY, DataLayer.mapOf(
                PROMOTIONS, listOf(
                    DataLayer.mapOf(
                        "creative_name", coupon.trackerModel.attribution,
                        "creative_slot", (position + 1).toString(),
                        "id", "${model.channelId}_${model.bannerId}_${model.categoryId}_${model.persoType}",
                        "name", "/ - p${model.parentPosition} - coupon widget - banner - ${coupon.trackerModel.gridId} - ${model.headerName}",
                    )
                )
            )
        )
    }
}
