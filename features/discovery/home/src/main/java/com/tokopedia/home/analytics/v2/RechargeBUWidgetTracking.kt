package com.tokopedia.home.analytics.v2

import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.presentation.adapter.viewholder.RechargeBUWidgetProductCardViewHolder.Companion.IMAGE_TYPE_FULL
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
        val persoType = data.channel.trackingAttributionModel.persoType.toIntOrZero()
        val promotions = data.data.items.mapIndexed { index, item ->
            val productName = if (item.mediaUrlType == IMAGE_TYPE_FULL) item.subtitle else item.title
            Promotion(
                id = "${data.channel.id}_0_0_$persoType",
                creative = "${getHeaderName(data.channel)} - $productName",
                name = "/ - p${data.channel.verticalPosition} - $RECHARGE_BU_WIDGET_NAME - $RECHARGE_BU_WIDGET_BANNER_CARD - ${getHeaderName(data.channel)}",
                position = (index + 1).toString()
            )
        }
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
            val persoType = data.channel.trackingAttributionModel.persoType.toIntOrZero()
            val item = data.data.items[position]
            val productName = if (item.mediaUrlType == IMAGE_TYPE_FULL) item.subtitle else item.title
            val promotion = Promotion(
                    id = "${data.channel.id}_0_0_$persoType",
                    creative = "${getHeaderName(data.channel)} - $productName",
                    name = "/ - p${data.channel.verticalPosition} - $RECHARGE_BU_WIDGET_NAME - $RECHARGE_BU_WIDGET_BANNER_CARD - ${getHeaderName(data.channel)}",
                    position = position.toString()
            )
            trackingQueue.putEETracking(getBasicPromotionClick(
                    Event.PROMO_CLICK,
                    RECHARGE_BU_WIDGET_EVENT_CATEGORY,
                    Action.CLICK_ON.format("$RECHARGE_BU_WIDGET_BANNER_CARD $RECHARGE_BU_WIDGET_NAME"),
                    "${data.channel.id} - ${getHeaderName(data.channel)}",
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
                Label.KEY to "${data.channel.id} - ${getHeaderName(data.channel)}",
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
                Label.KEY to "${data.channel.id} - ${getHeaderName(data.channel)}",
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
        val persoType = data.channel.trackingAttributionModel.persoType.toIntOrZero()
        val promotion = Promotion(
                id = "${data.channel.id}_0_0_$persoType",
                creative = data.data.mediaUrl,
                name = "/ - p${data.channel.verticalPosition} - $RECHARGE_BU_WIDGET_NAME - $RECHARGE_BU_WIDGET_BANNER_CARD - ${getHeaderName(data.channel)}",
                position = "1"
        )
        trackingQueue.putEETracking(getBasicPromotionClick(
                Event.PROMO_CLICK,
                RECHARGE_BU_WIDGET_EVENT_CATEGORY,
                Action.CLICK_ON.format("$RECHARGE_BU_WIDGET_BANNER $RECHARGE_BU_WIDGET_NAME"),
                "${data.channel.id} - ${getHeaderName(data.channel)}",
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
        val persoType = data.channel.trackingAttributionModel.persoType.toIntOrZero()
        val promotion = Promotion(
                id = "${data.channel.id}_0_0_$persoType",
                creative = data.data.mediaUrl,
                name = "/ - p${data.channel.verticalPosition} - $RECHARGE_BU_WIDGET_NAME - $RECHARGE_BU_WIDGET_BANNER - ${getHeaderName(data.channel)}",
                position = "1"
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

    private fun getHeaderName(channel: ChannelModel): String {
        return channel.widgetParam.removePrefix("?section=")
    }
}