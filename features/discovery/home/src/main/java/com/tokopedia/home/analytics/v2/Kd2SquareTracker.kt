package com.tokopedia.home.analytics.v2

import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelTracker
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object Kd2SquareTracker : BaseTrackerConst() {

    // TrackerID: 50030
    private const val IMPRESSION_TRACKER_ID = "50030"
    private const val IMPRESSION_ACTION = "impression on banner dynamic channel 2 square"
    fun widgetImpressed(model: ChannelModel, userId: String, position: Int): Map<String, Any> {
        val attribute = model.trackingAttributionModel

        val trackingBuilder = BaseTrackerBuilder().constructBasicPromotionView(
            event = HomePageTracking.PROMO_VIEW,
            eventAction = IMPRESSION_ACTION,
            eventCategory = Category.HOMEPAGE,
            eventLabel = "${attribute.channelId} - ${attribute.headerName}",
            promotions = model.channelGrids.mapIndexed { index, channelGrid ->
                Promotion(
                    id = "${attribute.channelId}_${attribute.bannerId}_${attribute.categoryId}_${attribute.persoType}",
                    name = "/ - p${position + 1} - dynamic channel 2 square - banner - ${attribute.headerName}",
                    position = (index + 1).toString(),
                    creative = channelGrid.attribution
                )
            }
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(attribute.channelId)
            .appendCampaignCode(attribute.campaignCode)
            .appendCustomKeyValue(TrackerId.KEY, IMPRESSION_TRACKER_ID)
            .appendUserId(userId)

        return trackingBuilder.build()
    }

    // TrackerID: 50031
    private const val CARD_CLICK_TRACKER_ID = "50031"
    private const val CARD_CLICK_ACTION = "click on banner dynamic channel 2 square"
    fun cardClicked(attribute: ChannelTracker, userId: String, position: Int): Map<String, Any> {

        val trackingBuilder = BaseTrackerBuilder().constructBasicPromotionClick(
            event = HomePageTracking.PROMO_CLICK,
            eventAction = CARD_CLICK_ACTION,
            eventCategory = Category.HOMEPAGE,
            eventLabel = "${attribute.channelId} - ${attribute.headerName}",
            promotions = listOf(
                Promotion(
                    id = "${attribute.channelId}_${attribute}bannerId_${attribute.categoryId}_${attribute}persoType", // TODO
                    name = "/ - p${position + 1} - dynamic channel 2 square - banner - ${attribute.headerName}",
                    position = (position + 1).toString(),
                    creative = "" //attribute.attribution // TODO
                )
            )
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(attribute.channelId)
            .appendCustomKeyValue(TrackerId.KEY, CARD_CLICK_TRACKER_ID)
            .appendCampaignCode(attribute.campaignCode)
            .appendUserId(userId)

        return trackingBuilder.build()
    }
}
