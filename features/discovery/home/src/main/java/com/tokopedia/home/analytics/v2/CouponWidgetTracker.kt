package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst
import com.tokopedia.home.analytics.HomePageTracking.PROMOTIONS
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.track.constant.TrackerConstant

object CouponWidgetTracker : BaseTracking() {

    private const val IMPRESSION_TRACKER_ID = 49608

    private const val ACTION_IMPRESSION = "impression on banner coupon widget"

    fun impress(
        position: Int,
        userId: String,
        model: TrackingAttributionModel,
        coupons: List<CouponWidgetDataItemModel>
    ): Map<String, Any?> {
        return DataLayer.mapOf(
            Event.KEY, DigitalTrackingConst.Event.VIEW_ITEM,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, ACTION_IMPRESSION,
            Label.KEY, "${model.channelId} - ${model.headerName}",
            TrackerConstant.TRACKER_ID, IMPRESSION_TRACKER_ID,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            ChannelId.KEY, model.channelId,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            TrackerConstant.USERID, userId,
            PROMOTIONS, coupons.mapIndexed { index, coupon ->
                DataLayer.mapOf(
                    "creative_name", coupon.trackerModel.attribution,
                    "creative_slot", (position + 1).toString(),
                    "item_id", "${model.channelId}_${model.bannerId}_${model.categoryId}_${model.persoType}",
                    "name", "/ - p${index} - coupon widget - banner - ${coupon.trackerModel.gridId} - ${model.headerName}",
                )
            }
        )
    }

    fun ctaClick() {

    }
}
