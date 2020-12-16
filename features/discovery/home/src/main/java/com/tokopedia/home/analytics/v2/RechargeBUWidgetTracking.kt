package com.tokopedia.home.analytics.v2

import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

object RechargeBUWidgetTracking : BaseTracking() {
    private const val RECHARGE_BU_WIDGET_CLICK_EVENT = "clickHomepage"
    private const val RECHARGE_BU_WIDGET_EVENT_CATEGORY = "homepage"
    private const val RECHARGE_BU_WIDGET_NAME = "dynamic channel bu carousel"
    private const val RECHARGE_BU_WIDGET_BUSINESS_UNIT = "home \u0026 browse"
    private const val RECHARGE_BU_WIDGET_CURRENT_SITE = "tokopediamarketplace"
    private const val RECHARGE_BU_WIDGET_BANNER = "banner"
    private const val RECHARGE_BU_WIDGET_BANNER_CARD = "banner card"

    fun homeRechargeBUWidgetImpressionTracker(
            trackingQueue: TrackingQueue,
            data: RechargeBUWidgetDataModel,
            userId: String
    ) {
        val promotions = data.data.items.mapIndexed { index, item ->  Promotion(
                id = data.channel.id,
                creative = "${data.data.title} - ${item.title}",
                name = "/ - p1 - $RECHARGE_BU_WIDGET_NAME - $RECHARGE_BU_WIDGET_BANNER_CARD - ${data.channel.widgetParam}",
                position = index.toString()
        )}
        trackingQueue.putEETracking(getBasicPromotionView(
                Event.PROMO_VIEW,
                RECHARGE_BU_WIDGET_EVENT_CATEGORY,
                Action.IMPRESSION_ON.format("$RECHARGE_BU_WIDGET_BANNER_CARD $RECHARGE_BU_WIDGET_NAME"),
                "",
                promotions,
                userId,
                currentSite = RECHARGE_BU_WIDGET_CURRENT_SITE,
                businessUnit = RECHARGE_BU_WIDGET_BUSINESS_UNIT
        ) as? HashMap<String, Any>)
    }

    fun homeRechargeBUWidgetProductCardClickTracker(
            trackingQueue: TrackingQueue,
            data: RechargeBUWidgetDataModel,
            position: Int,
            userId: String
    ) {
        if (position < data.data.items.size) {
            val item = data.data.items[position]
            val promotion = Promotion(
                    id = data.channel.id,
                    creative = "${data.data.title} - ${item.title}",
                    name = "/ - p1 - $RECHARGE_BU_WIDGET_NAME - $RECHARGE_BU_WIDGET_BANNER_CARD - ${data.channel.widgetParam}",
                    position = position.toString()
            )
            trackingQueue.putEETracking(getBasicPromotionClick(
                    Event.PROMO_CLICK,
                    RECHARGE_BU_WIDGET_EVENT_CATEGORY,
                    Action.CLICK_ON.format("$RECHARGE_BU_WIDGET_BANNER_CARD $RECHARGE_BU_WIDGET_NAME"),
                    "${data.channel.id} - ${data.channel.widgetParam}",
                    listOf(promotion),
                    userId,
                    currentSite = RECHARGE_BU_WIDGET_CURRENT_SITE,
                    businessUnit = RECHARGE_BU_WIDGET_BUSINESS_UNIT
            ) as? HashMap<String, Any>)
        }
    }

    fun homeRechargeBUWidgetViewAllButtonClickTracker(
            data: RechargeBUWidgetDataModel,
            userId: String
    ) {
        val map = mapOf<String, Any>(
                Event.KEY to RECHARGE_BU_WIDGET_CLICK_EVENT,
                Category.KEY to RECHARGE_BU_WIDGET_EVENT_CATEGORY,
                Action.KEY to "click view all on $RECHARGE_BU_WIDGET_NAME",
                Label.KEY to "${data.channel.id} - ${data.channel.widgetParam}",
                BusinessUnit.KEY to RECHARGE_BU_WIDGET_BUSINESS_UNIT,
                CurrentSite.KEY to RECHARGE_BU_WIDGET_CURRENT_SITE,
                UserId.KEY to userId
        )
        getTracker().sendGeneralEvent(map)
    }

    fun homeRechargeBUWidgetViewAllCardClickTracker(
            data: RechargeBUWidgetDataModel,
            userId: String
    ) {
        val map = mapOf<String, Any>(
                Event.KEY to RECHARGE_BU_WIDGET_CLICK_EVENT,
                Category.KEY to RECHARGE_BU_WIDGET_EVENT_CATEGORY,
                Action.KEY to "click view all card on $RECHARGE_BU_WIDGET_NAME",
                Label.KEY to "${data.channel.id} - ${data.channel.widgetParam}",
                BusinessUnit.KEY to RECHARGE_BU_WIDGET_BUSINESS_UNIT,
                CurrentSite.KEY to RECHARGE_BU_WIDGET_CURRENT_SITE,
                UserId.KEY to userId
        )
        getTracker().sendGeneralEvent(map)
    }

    fun homeRechargeBUWidgetBannerClickTracker(
            trackingQueue: TrackingQueue,
            data: RechargeBUWidgetDataModel,
            userId: String
    ) {
        val promotion = Promotion(
                id = data.channel.id,
                creative = data.data.mediaUrl,
                name = "/ - p1 - $RECHARGE_BU_WIDGET_NAME - $RECHARGE_BU_WIDGET_BANNER_CARD - ${data.channel.widgetParam}",
                position = "none/other"
        )
        trackingQueue.putEETracking(getBasicPromotionClick(
                Event.PROMO_CLICK,
                RECHARGE_BU_WIDGET_EVENT_CATEGORY,
                Action.CLICK_ON.format("$RECHARGE_BU_WIDGET_BANNER $RECHARGE_BU_WIDGET_NAME"),
                "${data.channel.id} - ${data.channel.widgetParam}",
                listOf(promotion),
                userId,
                currentSite = RECHARGE_BU_WIDGET_CURRENT_SITE,
                businessUnit = RECHARGE_BU_WIDGET_BUSINESS_UNIT
        ) as? HashMap<String, Any>)
    }

    fun homeRechargeBUWidgetBannerImpressionTracker(
            trackingQueue: TrackingQueue,
            data: RechargeBUWidgetDataModel,
            userId: String
    ) {
        val promotion = Promotion(
                id = data.channel.id,
                creative = data.data.mediaUrl,
                name = "/ - p1 - $RECHARGE_BU_WIDGET_NAME - $RECHARGE_BU_WIDGET_BANNER - ${data.channel.widgetParam}",
                position = "none/other"
        )
        trackingQueue.putEETracking(getBasicPromotionView(
                Event.PROMO_VIEW,
                RECHARGE_BU_WIDGET_EVENT_CATEGORY,
                Action.IMPRESSION_ON.format("$RECHARGE_BU_WIDGET_BANNER $RECHARGE_BU_WIDGET_NAME"),
                "",
                listOf(promotion),
                userId,
                currentSite = RECHARGE_BU_WIDGET_CURRENT_SITE,
                businessUnit = RECHARGE_BU_WIDGET_BUSINESS_UNIT
        ) as? HashMap<String, Any>)
    }
}