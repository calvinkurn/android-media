package com.tokopedia.home.analytics.v2

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by frenzel
 */
@SuppressLint("PII Data Exposure")
object TodoWidgetTracking : BaseTrackerConst() {
    private const val DYNAMIC_CHANNEL_TODO_WIDGET = "dynamic channel to do widget"
    private const val EVENT_ACTION_CLICK_CTA = "click CTA button on dynamic channel to do widget"
    private const val EVENT_ACTION_CLICK_CARD = "click on card on dynamic channel to do widget"
    private const val EVENT_ACTION_CLICK_CLOSE = "click close button on dynamic channel to do widget"
    private const val EVENT_ACTION_IMPRESSION = "impression on banner dynamic channel to do widget"
    private const val FORMAT_EVENT_LABEL = "%s_%s_%s_%s_%s_%s"
    private const val ITEM_ID_FORMAT = "%s_%s_%s_%s_%s_%s_%s"
    private const val ITEM_NAME_FORMAT = "/ - p%s - $DYNAMIC_CHANNEL_TODO_WIDGET - banner - %s"
    private const val TRACKER_ID_IMPRESSION = "41015"
    private const val TRACKER_ID_CLICK_CTA = "41016"
    private const val TRACKER_ID_CLICK_CARD = "43721"
    private const val TRACKER_ID_CLICK_CLOSE = "41017"
    private const val DEFAULT_ID = "0"

    fun getTodoWidgetView(element: CarouselTodoWidgetDataModel, userId: String): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val creativeSlot = (element.cardPosition + 1).toString()
        val itemId = ITEM_ID_FORMAT.format(
            element.channelId,
            DEFAULT_ID,
            element.data.dataSource,
            element.data.title,
            element.data.contextInfo,
            element.data.price,
            element.data.dueDate
        )
        val itemName = ITEM_NAME_FORMAT.format(
            element.verticalPosition,
            element.data.title
        )

        val listPromotions = arrayListOf(
            Promotion(
                creative = element.data.title,
                position = creativeSlot,
                id = itemId,
                name = itemName
            )
        )

        return trackingBuilder.constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = EVENT_ACTION_IMPRESSION,
            eventLabel = element.channelId,
            promotions = listPromotions
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendCustomKeyValue(TrackerId.KEY, TRACKER_ID_IMPRESSION)
            .appendChannelId(element.channelId)
            .build()
    }

    fun sendTodoWidgetCTAClicked(element: CarouselTodoWidgetDataModel, userId: String) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Action.KEY, EVENT_ACTION_CLICK_CTA)
        bundle.putString(Category.KEY, Category.HOMEPAGE)
        bundle.putString(Label.KEY, element.channelId)
        bundle.putString(TrackerId.KEY, TRACKER_ID_CLICK_CTA)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CampaignCode.KEY, DEFAULT_ID)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        bundle.putString(ChannelId.KEY, element.channelId)
        val promotion = Bundle().apply {
            putString(Promotion.CREATIVE_NAME, element.data.title)
            putString(Promotion.CREATIVE_SLOT, (element.cardPosition + 1).toString())
            putString(
                Promotion.ITEM_ID,
                ITEM_ID_FORMAT.format(
                    element.channelId,
                    DEFAULT_ID,
                    element.data.dataSource,
                    element.data.title,
                    element.data.contextInfo,
                    element.data.price,
                    element.data.dueDate
                )
            )
            putString(
                Promotion.ITEM_NAME,
                ITEM_NAME_FORMAT.format(
                    element.verticalPosition,
                    element.data.title
                )
            )
        }

        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    fun sendTodoWidgetCardClicked(element: CarouselTodoWidgetDataModel) {
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = Category.HOMEPAGE,
            eventAction = EVENT_ACTION_CLICK_CARD,
            eventLabel = FORMAT_EVENT_LABEL.format(
                element.channelId,
                element.data.dataSource,
                element.data.title,
                element.data.contextInfo,
                element.data.price,
                element.data.dueDate
            )
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(element.channelId)
            .appendCustomKeyValue(TrackerId.KEY, TRACKER_ID_CLICK_CARD)

        getTracker().sendGeneralEvent(trackerBuilder.build())
    }

    fun sendTodoWidgetCloseClicked(element: CarouselTodoWidgetDataModel) {
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = Category.HOMEPAGE,
            eventAction = EVENT_ACTION_CLICK_CLOSE,
            eventLabel = FORMAT_EVENT_LABEL.format(
                element.channelId,
                element.data.dataSource,
                element.data.title,
                element.data.contextInfo,
                element.data.price,
                element.data.dueDate
            )
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(element.channelId)
            .appendCustomKeyValue(TrackerId.KEY, TRACKER_ID_CLICK_CLOSE)

        getTracker().sendGeneralEvent(trackerBuilder.build())
    }
}
