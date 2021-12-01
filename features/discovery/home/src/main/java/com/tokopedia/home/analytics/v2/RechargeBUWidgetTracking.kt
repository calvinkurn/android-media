package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
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
    private const val RECHARGE_BU_WIDGET_NEW_NAME = "new bu widget"
    private const val RECHARGE_BU_WIDGET_BUSINESS_UNIT = "home \u0026 browse"
    private const val RECHARGE_BU_WIDGET_CURRENT_SITE = "tokopediamarketplace"
    private const val RECHARGE_BU_WIDGET_BANNER = "banner"
    private const val RECHARGE_BU_WIDGET_BANNER_CARD = "banner card"
    private const val RECHARGE_BU_WIDGET_PRODUCT_CARD = "product card"
    private const val KEY_PRODUCT_NAME = "name"
    private const val KEY_PRODUCT_ID = "id"
    private const val KEY_PRODUCT_PRICE = "price"
    private const val KEY_PRODUCT_BRAND = "brand"
    private const val KEY_PRODUCT_CATEGORY = "category"
    private const val KEY_PRODUCT_VARIANT = "variant"
    private const val KEY_PRODUCT_INDEX = "index"
    private const val KEY_PRODUCTS = "products"
    private const val KEY_EVENT_IMPRESSIONS = "impressions"
    private const val KEY_PRODUCT_LIST = "list"
    private const val KEY_CURRENCY_CODE = "currencyCode"
    private const val IDR_CURRENCY = "IDR"
    private const val DEFAULT_TRACKING_LABEL_VALUES = "-"

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
            val item = data.data.items[position]
            //empty supposed to be userType next dev.
            val eventLabel = String.format("%s - %s - %s - %d - %s - %s - %s - %d",
                DEFAULT_TRACKING_LABEL_VALUES,
                if (getHeaderName(data.channel).isNullOrBlank()) DEFAULT_TRACKING_LABEL_VALUES else getHeaderName(data.channel),
                if (item.trackingData.itemType.isNullOrBlank()) DEFAULT_TRACKING_LABEL_VALUES else item.trackingData.itemType,
                position + 1,
                if (item.trackingData.categoryId.isNullOrBlank()) DEFAULT_TRACKING_LABEL_VALUES else item.trackingData.categoryId,
                if (item.trackingData.operatorId.isNullOrBlank()) DEFAULT_TRACKING_LABEL_VALUES else item.trackingData.operatorId,
                if (item.trackingData.productId.isNullOrBlank()) DEFAULT_TRACKING_LABEL_VALUES else item.trackingData.productId,
                convertRupiahToInt(item.label2)
            )
            val bundle = DataLayer.mapOf(
                Event.KEY ,  Event.PRODUCT_CLICK,
                Action.KEY , Action.CLICK_ON.format("$RECHARGE_BU_WIDGET_PRODUCT_CARD $RECHARGE_BU_WIDGET_NAME"),
                Category.KEY , "$RECHARGE_BU_WIDGET_EVENT_CATEGORY - $RECHARGE_BU_WIDGET_NEW_NAME",
                Label.KEY , eventLabel,
                BusinessUnit.KEY , RECHARGE_BU_WIDGET_BUSINESS_UNIT,
                CurrentSite.KEY , RECHARGE_BU_WIDGET_CURRENT_SITE,
                Ecommerce.KEY , getProductClick(data, position),
                UserId.KEY , userId
            )
            trackingQueue.putEETracking(bundle as? HashMap<String, Any>)
        }
    }

    fun homeRechargeBUWidgetCardImpressionTracker(
        trackingQueue: TrackingQueue,
        data: RechargeBUWidgetDataModel,
        position: Int,
        userId: String) {

        if (position < data.data.items.size) {
            val item = data.data.items[position]
            //empty supposed to be userType next dev.
            val eventLabel = String.format("%s - %s - %s - %d - %s - %s - %s - %d",
                DEFAULT_TRACKING_LABEL_VALUES,
                if (getHeaderName(data.channel).isNullOrBlank()) DEFAULT_TRACKING_LABEL_VALUES else getHeaderName(data.channel),
                if (item.trackingData.itemType.isNullOrBlank()) DEFAULT_TRACKING_LABEL_VALUES else item.trackingData.itemType,
                position + 1,
                if (item.trackingData.categoryId.isNullOrBlank()) DEFAULT_TRACKING_LABEL_VALUES else item.trackingData.categoryId,
                if (item.trackingData.operatorId.isNullOrBlank()) DEFAULT_TRACKING_LABEL_VALUES else item.trackingData.operatorId,
                if (item.trackingData.productId.isNullOrBlank()) DEFAULT_TRACKING_LABEL_VALUES else item.trackingData.productId,
                convertRupiahToInt(item.label2)
            )

            val tracker = DataLayer.mapOf(
                Event.KEY,Event.PRODUCT_VIEW,
                Action.KEY,Action.IMPRESSION_ON.format("$RECHARGE_BU_WIDGET_PRODUCT_CARD $RECHARGE_BU_WIDGET_NAME"),
                Category.KEY,"$RECHARGE_BU_WIDGET_EVENT_CATEGORY - $RECHARGE_BU_WIDGET_NEW_NAME",
                Label.KEY, eventLabel,
                BusinessUnit.KEY,RECHARGE_BU_WIDGET_BUSINESS_UNIT,
                CurrentSite.KEY, RECHARGE_BU_WIDGET_CURRENT_SITE,
                Ecommerce.KEY, getProductView(data, position),
                UserId.KEY, userId
            )
            trackingQueue.putEETracking(tracker as? HashMap<String, Any>)
        }
    }

    private fun getProductClick(data: RechargeBUWidgetDataModel, position: Int) : Map<String, Any> {
        return mapOf(
            Event.CLICK to mapOf(
                KEY_PRODUCT_LIST to data.data.items[position].trackingData.itemType,
                KEY_PRODUCTS to listOf(
                    getProductData(data, position)
                )
            )
        )
    }

    private fun getProductView(data: RechargeBUWidgetDataModel, position: Int) : Map<String, Any>{
        return mapOf(
            KEY_CURRENCY_CODE to IDR_CURRENCY,
            KEY_EVENT_IMPRESSIONS to listOf(getProductData(data, position))
        )
    }

    private fun getProductData(data: RechargeBUWidgetDataModel, position: Int): Map<String, Any>{
        val persoType = data.channel.trackingAttributionModel.persoType.toIntOrZero()
        return mapOf(
            KEY_PRODUCT_INDEX to (position + 1).toString(),
            KEY_PRODUCT_BRAND to data.data.items[position].trackingData.operatorId,
            KEY_PRODUCT_CATEGORY to data.data.items[position].trackingData.categoryId,
            KEY_PRODUCT_ID to "${data.channel.id}_0_0_$persoType",
            KEY_PRODUCT_NAME to "/ - p${data.channel.verticalPosition} - $RECHARGE_BU_WIDGET_NAME - $RECHARGE_BU_WIDGET_BANNER_CARD - ${getHeaderName(data.channel)}",
            KEY_PRODUCT_VARIANT to data.data.items[position].trackingData.itemType,
            KEY_PRODUCT_PRICE to convertRupiahToInt(data.data.items[position].label2)
        )
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