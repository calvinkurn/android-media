package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.HomePageTracking.PROMOTIONS
import com.tokopedia.home.analytics.HomePageTracking.PROMO_CLICK
import com.tokopedia.home.analytics.HomePageTracking.PROMO_VIEW
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.constant.TrackerConstant

object Kd4SquareTracker : BaseTrackerConst() {

    // TrackerID: 50016
    private const val IMPRESSION_TRACKER_ID = "50016"
    private const val IMPRESSION_ACTION = "impression on banner dynamic channel 4 square"
    fun widgetImpressed(model: ChannelModel, userId: String, position: Int): Map<String, Any> {
        val attribute = model.trackingAttributionModel

        return DataLayer.mapOf(
            Event.KEY, PROMO_VIEW,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, IMPRESSION_ACTION,
            Label.KEY, "${attribute.channelId} - ${attribute.headerName}",
            TrackerConstant.TRACKER_ID, IMPRESSION_TRACKER_ID,
            CampaignCode.KEY, attribute.campaignCode,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            ChannelId.KEY, attribute.channelId,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            TrackerConstant.USERID, userId,
            PROMOTIONS, model.channelGrids.mapIndexed { index, grid ->
                DataLayer.mapOf(
                    "creative_name", grid.attribution,
                    "creative_slot", (position + 1).toString(),
                    "item_id", "${attribute.channelId}_${attribute.bannerId}_${attribute.categoryId}_${attribute.persoType}",
                    "name", "/ - p${index} - dynamic channel 4 square - banner - ${attribute.headerName}",
                )
            }
        )
    }

    // TrackerID: 50021
    private const val CARD_CLICK_TRACKER_ID = "50021"
    private const val CARD_CLICK_ACTION = "click on banner dynamic channel 4 square"
    fun cardClicked(model: ChannelModel, channelGrid: ChannelGrid, userId: String, position: Int): Map<String, Any> {
        val attribute = model.trackingAttributionModel

        return DataLayer.mapOf(
            Event.KEY, PROMO_CLICK,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CARD_CLICK_ACTION,
            Label.KEY, "${attribute.channelId} - ${attribute.headerName}",
            TrackerConstant.TRACKER_ID, CARD_CLICK_TRACKER_ID,
            CampaignCode.KEY, attribute.campaignCode,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            ChannelId.KEY, attribute.channelId,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            TrackerConstant.USERID, userId,
            PROMOTIONS, listOf(
                DataLayer.mapOf(
                    "creative_name", channelGrid.attribution,
                    "creative_slot", (position + 1).toString(),
                    "item_id", "${attribute.channelId}_${attribute.bannerId}_${attribute.categoryId}_${attribute.persoType}",
                    "name", "/ - p${position + 1} - dynamic channel 4 square - banner - ${attribute.headerName}",
                )
            )
        )
    }

    // TrackerID: 50022
    private const val CHEVRON_CLICK_TRACKER_ID = "50022"
    private const val CHEVRON_CLICK_ACTION = "click view all chevron on dynamic channel 4 square"
    fun viewAllChevronClicked(model: ChannelModel): Map<String, Any> {
        val attribute = model.trackingAttributionModel

        return DataLayer.mapOf(
            Event.KEY, PROMO_CLICK,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CHEVRON_CLICK_ACTION,
            Label.KEY, "${attribute.channelId} - ${attribute.headerName}",
            TrackerConstant.TRACKER_ID, CHEVRON_CLICK_TRACKER_ID,
            CampaignCode.KEY, attribute.campaignCode,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            ChannelId.KEY, attribute.channelId,
            CurrentSite.KEY, CurrentSite.DEFAULT
        )
    }
}
