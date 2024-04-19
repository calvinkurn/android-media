package com.tokopedia.home.analytics.v2

import android.annotation.SuppressLint
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.core.analytics.nishikino.model.Campaign
import com.tokopedia.home_component.visitable.shorten.ItemDealsWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.MissionWidgetUiModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object TwoSquareWidgetTracking : BaseTrackerConst() {

    private const val EVENT_ACTION_IMPRESSION = "impression on banner dynamic channel 2 square"
    private const val EVENT_LABEL = "%s - %s"
    private const val EVENT_ACTION_CLICK = "click on banner dynamic channel 2 square"
    private const val EVENT_ACTION_CLICK_CHEVRON = "click view all chevron on dynamic channel 2 square"
    private const val ITEM_ID_PROMOTION_FORMAT = "%s_%s_0_"

    fun impressCardDeals(data: ItemDealsWidgetUiModel, position: Int, userid: String): Map<String, Any> {
        return impressCard(data.channelId, data.headerName, position, userid, data.gridId, data.creativeName, data.campaignCode)
    }

    private fun impressCard(channelId : String, headerName : String, position: Int, userid: String,
                            gridId : String, creativeName : String, campaignCode: String): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder().constructBasicPromotionView(
                Event.VIEW_ITEM,
                Category.HOMEPAGE,
                EVENT_ACTION_IMPRESSION,
                String.format(EVENT_LABEL, channelId, headerName),
                listOf(Promotion(
                    id = ITEM_ID_PROMOTION_FORMAT.format(channelId, gridId),
                    name = "/ - p${position} - dynamic channel 2 square - banner - ${headerName}",
                    creative = creativeName,
                    position = position.toString()))
        ).appendUserId(userid)
                .appendCustomKeyValue(TrackerId.KEY, "50030")
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCampaignCode(campaignCode)
                .appendChannelId(channelId)
                .appendCurrentSite(CurrentSite.DEFAULT)
        return trackingBuilder.build()
    }

    fun clickCardDeals(data: ItemDealsWidgetUiModel, position: Int, userid: String) : Map<String, Any> {
        return clickCard(data.channelId, data.headerName, position, userid, data.gridId, data.creativeName, data.campaignCode)
    }

    private fun clickCard(channelId : String, headerName : String, position: Int, userid: String,
                          gridId : String, creativeName : String, campaignCode: String): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder().constructBasicPromotionClick(
                Event.SELECT_CONTENT,
                Category.HOMEPAGE,
                EVENT_ACTION_CLICK,
                String.format(EVENT_LABEL, channelId, headerName),
                listOf(Promotion(
                    id = ITEM_ID_PROMOTION_FORMAT.format(channelId, gridId),
                    name = "/ - p${position} - dynamic channel 2 square - banner - ${headerName}",
                    creative = creativeName,
                    position = position.toString()))
        ).appendUserId(userid)
            .appendCustomKeyValue(TrackerId.KEY, "50031")
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCampaignCode(campaignCode)
            .appendChannelId(channelId)
            .appendCurrentSite(CurrentSite.DEFAULT)

        return trackingBuilder.build()
    }

   /* @SuppressLint("VisibleForTests")
    fun clickArrow() : Map<String, Any>{
        return DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Action.KEY, EVENT_ACTION_CLICK_CHEVRON,
            Category.KEY, Category.HOMEPAGE,
            Label.KEY, String.format(EVENT_LABEL, data.channelId, data.headerName),
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CampaignCode.KEY, data.campaignCode,
            ChannelId.KEY, data.channelId,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            TrackerId.KEY, "50032"
        ) as Map<String, Any>
    }*/
}
