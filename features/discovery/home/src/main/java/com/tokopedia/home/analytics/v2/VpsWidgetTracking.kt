package com.tokopedia.home.analytics.v2

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.HomePageTracking.FORMAT_4_VALUE_UNDERSCORE
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.homenav.common.TrackingConst
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_VIEW

/**
 * Created by frenzel
 */
object VpsWidgetTracking : BaseTrackerConst() {
    private const val VPS_NAME = "lego banner 4 image auto v2"
    private const val PROMOTION_NAME = "/ - p%s - %s - %s - %s"
    private const val FORMAT_3_ITEMS = "%s - %s - %s"
    private const val CLICK_VIEW_ALL = "click view all on $VPS_NAME"
    private const val TRACKER_ID_IMPRESSION = "31870"
    private const val TRACKER_ID_CLICK = "31871"
    private const val TRACKER_ID_CLICK_VIEW_ALL = "43643"

    fun getVpsImpression(channel: ChannelModel, position: Int, userId: String): HashMap<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
            event = PROMO_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = Action.IMPRESSION.format(VPS_NAME),
            eventLabel = FORMAT_3_ITEMS.format(channel.id, channel.layout, channel.channelHeader.name),
            promotions = channel.channelGrids.mapIndexed { index, grid ->
                Promotion(
                    id = FORMAT_4_VALUE_UNDERSCORE.format(
                        channel.id,
                        grid.id,
                        channel.trackingAttributionModel.persoType,
                        channel.trackingAttributionModel.categoryId
                    ),
                    creative = grid.attribution,
                    name = PROMOTION_NAME.format(position, VPS_NAME, channel.layout, channel.channelHeader.name),
                    position = (index + 1).toString()
                )
            }
        )
            .appendChannelId(channel.id)
            .appendBusinessUnit(TrackingConst.DEFAULT_BUSINESS_UNIT)
            .appendCurrentSite(TrackingConst.DEFAULT_CURRENT_SITE)
            .appendUserId(userId)
            .appendCustomKeyValue(TrackerId.KEY, TRACKER_ID_IMPRESSION)
            .build() as HashMap<String, Any>
    }

    private fun getVpsItemClick(channel: ChannelModel, grid: ChannelGrid, position: Int, userId: String): Bundle {
        return Bundle().apply {
            putString(Event.KEY, Event.SELECT_CONTENT)
            putString(Action.KEY, Action.CLICK.format(VPS_NAME))
            putString(Category.KEY, Category.HOMEPAGE)
            putString(
                Label.KEY,
                FORMAT_3_ITEMS.format(
                    channel.id,
                    channel.layout,
                    channel.channelHeader.name
                )
            )
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            putString(CampaignCode.KEY, grid.campaignCode)
            putString(UserId.KEY, userId)
            val promotion = Bundle().apply {
                putString(
                    Promotion.CREATIVE_NAME,
                    channel.trackingAttributionModel.promoName
                )
                putString(Promotion.CREATIVE_SLOT, (position + 1).toString())
                putString(
                    Promotion.ITEM_ID,
                    FORMAT_4_VALUE_UNDERSCORE.format(
                        channel.id,
                        grid.id,
                        channel.trackingAttributionModel.persoType,
                        channel.trackingAttributionModel.categoryId
                    )
                )
                putString(
                    Promotion.ITEM_NAME,
                    PROMOTION_NAME.format(channel.verticalPosition, VPS_NAME, channel.layout, channel.channelHeader.name)
                )
            }
            putString(ChannelId.KEY, channel.id)
            putString(TrackerId.KEY, TRACKER_ID_CLICK)
            putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        }
    }

    fun sendVpsItemClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) {
        getTracker().sendEnhanceEcommerceEvent(
            Event.SELECT_CONTENT,
            getVpsItemClick(channelModel, channelGrid, position, userId)
        )
    }

    fun getVpsViewAllClick(channelModel: ChannelModel): HashMap<String, Any> {
        return DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Action.KEY, CLICK_VIEW_ALL,
            Category.KEY, Category.HOMEPAGE,
            Label.KEY, FORMAT_3_ITEMS.format(channelModel.id, channelModel.layout, channelModel.channelHeader.name),
            TrackerId.KEY, TRACKER_ID_CLICK_VIEW_ALL,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            ChannelId.KEY, channelModel.id,
            CurrentSite.KEY, CurrentSite.DEFAULT
        ) as HashMap<String, Any>
    }
}
