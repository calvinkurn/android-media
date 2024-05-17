package com.tokopedia.home.analytics.v2

import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelTracker
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object Kd2BannerSquareTracker : BaseTrackerConst() {

    // TrackerID: 50030
    private const val IMPRESSION_TRACKER_ID = "50030"
    private const val IMPRESSION_ACTION = "impression on banner ${Const.ITEM_NAME}"
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
                    name = "/ - p${position + 1} - ${Const.ITEM_NAME} - banner - ${attribute.headerName}",
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
    private const val CARD_CLICK_ACTION = "click on banner ${Const.ITEM_NAME}"
    fun cardClicked(attribute: ChannelTracker, userId: String, position: Int): Map<String, Any> {

        val trackingBuilder = BaseTrackerBuilder().constructBasicPromotionClick(
            event = HomePageTracking.PROMO_CLICK,
            eventAction = CARD_CLICK_ACTION,
            eventCategory = Category.HOMEPAGE,
            eventLabel = "${attribute.channelId} - ${attribute.headerName}",
            promotions = listOf(
                Promotion(
                    id = "${attribute.channelId}_${attribute.bannerId}_${attribute.categoryId}_${attribute.persoType}",
                    name = "/ - p${position + 1} - ${Const.ITEM_NAME} - banner - ${attribute.headerName}",
                    position = (position + 1).toString(),
                    creative = attribute.attribution
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

    // TrackerID: 50032
    private const val CHEVRON_CLICK_TRACKER_ID = "50032"
    private const val CHEVRON_CLICK_ACTION = "click view all chevron on ${Const.ITEM_NAME}"
    fun channelHeaderClicked(model: ChannelModel): Map<String, Any> {
        val attribute = model.trackingAttributionModel

        val trackingBuilder = BaseTrackerBuilder().constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
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

    object Const {
        const val ITEM_NAME = "dynamic channel 2 square"
    }
}
