package com.tokopedia.home.analytics.v2

import com.tokopedia.home.analytics.HomePageTracking.PROMO_CLICK
import com.tokopedia.home.analytics.HomePageTracking.PROMO_VIEW
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object Kd4SquareTracker : BaseTrackerConst() {

    // TrackerID: 50016
    private const val IMPRESSION_TRACKER_ID = "50016"
    private const val IMPRESSION_ACTION = "impression on banner dynamic channel 4 square"
    fun widgetImpressed(model: ChannelModel, userId: String, position: Int): Map<String, Any> {
        val attribute = model.trackingAttributionModel

        val trackingBuilder = BaseTrackerBuilder().constructBasicPromotionView(
            event = PROMO_VIEW,
            eventAction = IMPRESSION_ACTION,
            eventCategory = Category.HOMEPAGE,
            eventLabel = "${attribute.channelId} - ${attribute.headerName}",
            promotions = model.channelGrids.mapIndexed { index, grid ->
                Promotion(
                    id = "${attribute.channelId}_${attribute.bannerId}_${attribute.categoryId}_${attribute.persoType}",
                    name = "/ - p${index} - dynamic channel 4 square - banner - ${attribute.headerName}",
                    position = (position + 1).toString(),
                    creative = grid.attribution
                )
            }
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(attribute.channelId)
            .appendCustomKeyValue(TrackerId.KEY, IMPRESSION_TRACKER_ID)
            .appendUserId(userId)

        return trackingBuilder.build()
    }

    // TrackerID: 50021
    private const val CARD_CLICK_TRACKER_ID = "50021"
    private const val CARD_CLICK_ACTION = "click on banner dynamic channel 4 square"
    fun cardClicked(model: ChannelModel, channelGrid: ChannelGrid, userId: String, position: Int): Map<String, Any> {
        val attribute = model.trackingAttributionModel

        val trackingBuilder = BaseTrackerBuilder().constructBasicPromotionClick(
            event = PROMO_CLICK,
            eventAction = CARD_CLICK_ACTION,
            eventCategory = Category.HOMEPAGE,
            eventLabel = "${attribute.channelId} - ${attribute.headerName}",
            promotions = listOf(
                Promotion(
                    id = "${attribute.channelId}_${attribute.bannerId}_${attribute.categoryId}_${attribute.persoType}",
                    name = "/ - p${position + 1} - dynamic channel 4 square - banner - ${attribute.headerName}",
                    position = (position + 1).toString(),
                    creative = channelGrid.attribution
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

    // TrackerID: 50022
    private const val CHEVRON_CLICK_TRACKER_ID = "50022"
    private const val CHEVRON_CLICK_ACTION = "click view all chevron on dynamic channel 4 square"
    fun viewAllChevronClicked(model: ChannelModel): Map<String, Any> {
        val attribute = model.trackingAttributionModel

        val trackingBuilder = BaseTrackerBuilder().constructBasicGeneralClick(
            event = PROMO_CLICK,
            eventAction = CHEVRON_CLICK_ACTION,
            eventCategory = Category.HOMEPAGE,
            eventLabel = "${attribute.channelId} - ${attribute.headerName}"
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(attribute.channelId)
            .appendCustomKeyValue(TrackerId.KEY, CHEVRON_CLICK_TRACKER_ID)
            .appendCampaignCode(attribute.campaignCode)

        return trackingBuilder.build()
    }
}
