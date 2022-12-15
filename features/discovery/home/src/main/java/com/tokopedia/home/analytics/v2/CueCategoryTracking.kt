package com.tokopedia.home.analytics.v2

import android.os.Bundle
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by dhaba
 */
object CueCategoryTracking : BaseTrackerConst() {
    private class CustomAction {
        companion object {
            const val EVENT_ACTION_CLICK = "click on cue widget banner"
            const val EVENT_ACTION_IMPRESSED = "impression on cue widget banner"
            const val PROMOTION_ID_IMPRESSED_FORMAT = "%s_%s_%s_%s"
            const val DEFAULT_VALUE = ""
            const val CUE_WIDGET_BANNER = "cue widget banner"
            const val PROMOTION_NAME_IMPRESSED_FORMAT = "/ - p%s - %s - %s - %s"
            const val CREATIVE_NAME = "category"
            const val EVENT_LABEL_FORMAT = "%s - %s - %s"
        }
    }

    fun getCueWidgetClick(
        channelGrid: ChannelGrid,
        userId: String,
        position: Int,
        channel: ChannelModel,
        positionVerticalWidget: Int,
        widgetGridType: String
    ): Pair<String, Bundle> {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(
            Action.KEY,
            CustomAction.EVENT_ACTION_CLICK
        )
        bundle.putString(Category.KEY, Category.HOMEPAGE)
        bundle.putString(
            Label.KEY,
            CustomAction.EVENT_LABEL_FORMAT.format(
                channel.id,
                widgetGridType,
                channel.channelHeader.name
            )
        )
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(Label.CAMPAIGN_CODE, channelGrid.campaignCode)
        bundle.putString(Label.CHANNEL_LABEL, channel.id)
        val promotion = Bundle()
        promotion.putString(
            Promotion.CREATIVE_NAME,
            CustomAction.CREATIVE_NAME
        )
        promotion.putString(Promotion.CREATIVE_SLOT, (position + 1).toString())
        val itemId = CustomAction.PROMOTION_ID_IMPRESSED_FORMAT.format(
            channel.id,
            CustomAction.DEFAULT_VALUE,
            channel.trackingAttributionModel.persoType,
            channelGrid.id
        )
        val itemName = CustomAction.PROMOTION_NAME_IMPRESSED_FORMAT.format(
            positionVerticalWidget,
            CustomAction.CUE_WIDGET_BANNER,
            widgetGridType,
            channel.channelHeader.name
        )
        promotion.putString(Promotion.ITEM_ID, itemId)
        promotion.putString(Promotion.ITEM_NAME, itemName)
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        bundle.putString(UserId.KEY, userId)
        return Pair(Event.SELECT_CONTENT, bundle)
    }

    fun getCueCategoryView(
        channelGrid: ChannelGrid,
        userId: String,
        position: Int,
        channel: ChannelModel,
        positionVerticalWidget: Int,
        widgetGridType: String
    ): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val creativeName = CustomAction.CREATIVE_NAME
        val promotionPosition = (position + 1).toString()
        val promotionId = CustomAction.PROMOTION_ID_IMPRESSED_FORMAT.format(
            channel.id,
            CustomAction.DEFAULT_VALUE,
            channel.trackingAttributionModel.persoType,
            channelGrid.id
        )
        val promotionName = CustomAction.PROMOTION_NAME_IMPRESSED_FORMAT.format(
            positionVerticalWidget,
            CustomAction.CUE_WIDGET_BANNER,
            widgetGridType,
            channel.channelHeader.name
        )
        val listPromotions = arrayListOf(
            Promotion(
                creative = creativeName,
                position = promotionPosition,
                id = promotionId,
                name = promotionName
            )
        )
        return trackingBuilder.constructBasicPromotionView(
                event = Event.PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = CustomAction.EVENT_ACTION_IMPRESSED,
                eventLabel = Label.NONE,
                promotions = listPromotions)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
    }
}
