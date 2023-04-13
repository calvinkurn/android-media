package com.tokopedia.home.analytics.v2

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.analytics.v2.TodoWidgetTracking.CustomAction.Companion.EVENT_ACTION_CLICK_CLOSE
import com.tokopedia.home.analytics.v2.TodoWidgetTracking.CustomAction.Companion.EVENT_ACTION_IMPRESSION
import com.tokopedia.home.analytics.v2.TodoWidgetTracking.CustomAction.Companion.FORMAT_DASH_TWO_VALUES
import com.tokopedia.home.analytics.v2.TodoWidgetTracking.CustomAction.Companion.ITEM_ID_FORMAT
import com.tokopedia.home.analytics.v2.TodoWidgetTracking.CustomAction.Companion.ITEM_NAME_FORMAT
import com.tokopedia.home.analytics.v2.TodoWidgetTracking.CustomAction.Companion.TRACKER_ID_CLICK_CLOSE
import com.tokopedia.home.analytics.v2.TodoWidgetTracking.CustomAction.Companion.TRACKER_ID_CLICK_CTA
import com.tokopedia.home.analytics.v2.TodoWidgetTracking.CustomAction.Companion.TRACKER_ID_IMPRESSION
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by frenzel
 */
@SuppressLint("PII Data Exposure")
object TodoWidgetTracking : BaseTrackerConst() {
    private class CustomAction {
        companion object {
            const val DYNAMIC_CHANNEL_TODO_WIDGET = "dynamic channel to do widget"
            const val EVENT_ACTION_CLICK_CTA = "click CTA button on dynamic channel to do widget"
            const val EVENT_ACTION_CLICK_CLOSE = "click close button on dynamic channel to do widget"
            const val EVENT_ACTION_IMPRESSION = "impression on banner dynamic channel to do widget"
            const val DEFAULT_VALUE = ""
            const val DEFAULT_BANNER_ID = "0"
            const val FORMAT_DASH_TWO_VALUES = "%s - %s"
            const val ITEM_ID_FORMAT = "%s_%s_%s_%s"
            const val ITEM_NAME_FORMAT = "/ - p%s - $DYNAMIC_CHANNEL_TODO_WIDGET - banner - %s"
            const val TRACKER_ID_IMPRESSION = "41015"
            const val TRACKER_ID_CLICK_CTA = "41016"
            const val TRACKER_ID_CLICK_CLOSE = "41017"
        }
    }

    fun getTodoWidgetView(element: CarouselTodoWidgetDataModel, horizontalPosition: Int, userId: String): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val creativeSlot = (horizontalPosition + 1).toString()
        val itemId = ITEM_ID_FORMAT.format(
            element.channel.id,
            element.channel.channelBanner.id,
            element.channel.trackingAttributionModel.persoType,
            element.channel.trackingAttributionModel.categoryId
        )
        val itemName = ITEM_NAME_FORMAT.format(
            element.verticalPosition,
            element.channel.channelHeader.name
        )

        // make sure lagi titlenya
        val listPromotions = arrayListOf(
            Promotion(
                creative = element.title,
                position = creativeSlot,
                id = itemId,
                name = itemName
            )
        )

        return trackingBuilder.constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = EVENT_ACTION_IMPRESSION,
            eventLabel = FORMAT_DASH_TWO_VALUES.format(element.channel.id, element.dataSource),
            promotions = listPromotions
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendCustomKeyValue(TrackerId.KEY, TRACKER_ID_IMPRESSION)
            .appendChannelId(element.channel.id)
            .build()
    }

    fun sendTodoWidgetCTAClicked(element: CarouselTodoWidgetDataModel, horizontalPosition: Int, userId: String) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Action.KEY, CustomAction.EVENT_ACTION_CLICK_CTA)
        bundle.putString(Category.KEY, Category.HOMEPAGE)
        bundle.putString(
            Label.KEY,
            FORMAT_DASH_TWO_VALUES.format(element.channel.id, element.dataSource)
        )
        bundle.putString(TrackerId.KEY, TRACKER_ID_CLICK_CTA)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CampaignCode.KEY, element.channel.trackingAttributionModel.campaignCode)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        bundle.putString(ChannelId.KEY, element.channel.id)
        val promotion = Bundle().apply {
            putString(Promotion.CREATIVE_NAME, element.title)
            putString(Promotion.CREATIVE_SLOT, (horizontalPosition + 1).toString())
            putString(
                Promotion.ITEM_ID,
                ITEM_ID_FORMAT.format(
                    element.channel.id,
                    element.channel.channelBanner.id,
                    element.channel.trackingAttributionModel.persoType,
                    element.channel.trackingAttributionModel.categoryId
                )
            )
            putString(
                Promotion.ITEM_NAME,
                ITEM_NAME_FORMAT.format(
                    element.verticalPosition,
                    element.channel.channelHeader.name
                )
            )
        }

        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    fun sendTodoWidgetCloseClicked(element: CarouselTodoWidgetDataModel) {
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = Category.HOMEPAGE,
            eventAction = EVENT_ACTION_CLICK_CLOSE,
            eventLabel = FORMAT_DASH_TWO_VALUES.format(element.channel.id, element.dataSource)
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(element.channel.id)
            .appendCustomKeyValue(TrackerId.KEY, TRACKER_ID_CLICK_CLOSE)

        getTracker().sendGeneralEvent(trackerBuilder.build())
    }
}
