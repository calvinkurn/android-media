package com.tokopedia.home.analytics.v2

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by yfsx on 12/10/21.
 */
object CampaignWidgetTracking : BaseTrackerConst() {

    const val IMPRESSION_ACTION_CAMPAIGN_WIDGET = "impression on banner dynamic channel campaign"
    const val CLICK_ACTION_CAMPAIGN_WIDGET = "click on banner dynamic channel campaign"
    const val FORMAT_CREATIVE = "%s - %s"
    const val FORMAT_NAME = "/ - p%s - dynamic channel campaign - banner - %s"
    const val FORMAT_LABEL_CLICK = "%s - %s"
    const val FORMAT_PROMO_ID = "%s - %s - %s - %s"
    const val CLICK_SEE_ALL_CARD = "click view all card on dynamic channel campaign"
    const val CLICK_SEE_ALL_CHANNEL = "click view all on dynamic channel campaign"

    fun getCampaignWidgetItemImpressionTracking(
        grid: ChannelGrid,
        channel: ChannelModel,
        position: Int,
        adapterPosition: Int,
        userId: String
    ): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder().constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventAction = IMPRESSION_ACTION_CAMPAIGN_WIDGET,
            eventCategory = Category.HOMEPAGE,
            eventLabel = "",
            promotions = listOf(
                Promotion(
                    id = String.format(
                        FORMAT_PROMO_ID,
                        grid.id,
                        channel.id,
                        channel.trackingAttributionModel.persoType,
                        channel.trackingAttributionModel.categoryId
                    ),
                    name = String.format(FORMAT_NAME, adapterPosition, channel.channelHeader.name),
                    position = (position + 1).toString(),
                    creative = String.format(FORMAT_CREATIVE, channel.name, grid.name),
                    creativeUrl = ""
                )
            )
        ).appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
        return trackingBuilder.build()
    }

    fun getCampaignWidgetItemClickTracking(
        grid: ChannelGrid,
        channel: ChannelModel,
        position: Int,
        adapterPosition: Int,
        userId: String
    ): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder().constructBasicPromotionClick(
            event = Event.PROMO_CLICK,
            eventAction = CLICK_ACTION_CAMPAIGN_WIDGET,
            eventCategory = Category.HOMEPAGE,
            eventLabel = String.format(FORMAT_LABEL_CLICK, channel.id, channel.channelHeader.name),
            promotions = listOf(
                Promotion(
                    id = String.format(
                        FORMAT_PROMO_ID,
                        grid.id,
                        channel.id,
                        channel.trackingAttributionModel.persoType,
                        channel.trackingAttributionModel.categoryId
                    ),
                    name = String.format(FORMAT_NAME, adapterPosition, channel.channelHeader.name),
                    position = (position + 1).toString(),
                    creative = String.format(FORMAT_CREATIVE, channel.name, grid.name),
                    creativeUrl = ""
                )
            )
        ).appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendAffinity(channel.trackingAttributionModel.persona)
            .appendAttribution(channel.trackingAttributionModel.galaxyAttribution)
        return trackingBuilder.build()
    }

    fun getSeeAllCardClickTracking(channel: ChannelModel): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder().constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventAction = CLICK_SEE_ALL_CARD,
            eventCategory = Category.HOMEPAGE,
            eventLabel = String.format(FORMAT_LABEL_CLICK, channel.id, channel.channelHeader.name)
        ).appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(channel.id)
        return trackingBuilder.build()
    }

    fun getSeeAllClickTracking(channel: ChannelModel): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder().constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventAction = CLICK_SEE_ALL_CHANNEL,
            eventCategory = Category.HOMEPAGE,
            eventLabel = String.format(FORMAT_LABEL_CLICK, channel.id, channel.channelHeader.name)
        ).appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(channel.id)
        return trackingBuilder.build()
    }

}