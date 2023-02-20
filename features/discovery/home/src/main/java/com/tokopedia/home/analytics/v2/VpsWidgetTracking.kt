package com.tokopedia.home.analytics.v2

import android.os.Bundle
import com.tokopedia.home.analytics.HomePageTracking.FORMAT_4_VALUE_UNDERSCORE
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.homenav.common.TrackingConst
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_VIEW
import com.tokopedia.track.builder.util.BaseTrackerConst.Label.FORMAT_2_ITEMS

/**
 * Created by frenzel
 */
object VpsWidgetTracking : BaseTrackerConst() {
    private const val VPS_NAME = "lego banner 4 image auto v2"

    fun getVpsImpression(channel: ChannelModel, position: Int, userId: String): HashMap<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
                event = PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION.format(VPS_NAME),
                eventLabel = Label.NONE,
                promotions = channel.channelGrids.mapIndexed { index, grid ->
                Promotion(
                        id = FORMAT_4_VALUE_UNDERSCORE.format(
                            channel.id, grid.id,
                            channel.trackingAttributionModel.persoType,
                            channel.trackingAttributionModel.categoryId
                        ),
                        creative = grid.attribution,
                        name = Ecommerce.PROMOTION_NAME.format(position, VPS_NAME, channel.channelHeader.name),
                        position = (index + 1).toString()
                )})
                .appendBusinessUnit(TrackingConst.DEFAULT_BUSINESS_UNIT)
                .appendCurrentSite(TrackingConst.DEFAULT_CURRENT_SITE)
                .appendUserId(userId)
                .build() as HashMap<String, Any>
    }

    private fun getVpsItemClick(channel: ChannelModel, grid: ChannelGrid, position: Int, userId: String): Bundle {
        return Bundle().apply {
            putString(Event.KEY, Event.SELECT_CONTENT)
            putString(Action.KEY, Action.CLICK.format(VPS_NAME))
            putString(Category.KEY, Category.HOMEPAGE)
            putString(
                Label.KEY,
                FORMAT_2_ITEMS.format(
                    channel.id, channel.channelHeader.name
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
                        channel.id, grid.id,
                        channel.trackingAttributionModel.persoType,
                        channel.trackingAttributionModel.categoryId
                    )
                )
                putString(
                    Promotion.ITEM_NAME,
                    Ecommerce.PROMOTION_NAME.format(channel.verticalPosition, VPS_NAME, channel.channelHeader.name)
                )
            }
            putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        }
    }

    fun sendVpsItemClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) {
        getTracker().sendEnhanceEcommerceEvent(
            Event.SELECT_CONTENT,
            getVpsItemClick(channelModel, channelGrid, position, userId)
        )
    }

}
