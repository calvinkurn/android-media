package com.tokopedia.sellerhome.analytic

import com.tokopedia.sellerhome.view.model.CarouselItemUiModel
import com.tokopedia.track.TrackApp

/**
 * Created By @ilhamsuaib on 2020-02-11
 */

object SellerHomeTracking {

    fun sendImpressionCardEvent(dataKey: String, state: String, cardValue: String) {
        val map = createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_CARD, "$dataKey $state").joinToString(" - "),
                cardValue
        )
        sendGeneralEvent(map)
    }

    fun sendClickCardEvent(dataKey: String, state: String, cardValue: String) {
        val map = createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_CARD, "$dataKey $state").joinToString(" - "),
                cardValue
        )
        sendGeneralEvent(map)
    }

    fun sendImpressionLineGraphEvent(dataKey: String, cardValue: String) {
        val map = createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_LINE_GRAPH, dataKey).joinToString(" - "),
                cardValue
        )
        sendGeneralEvent(map)
    }

    fun sendClickLineGraphEvent(dataKey: String, cardValue: String) {
        val map = createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_LINE_GRAPH, dataKey, TrackingConstant.SEE_MORE).joinToString(" - "),
                cardValue
        )
        sendGeneralEvent(map)
    }

    fun sendImpressionDescriptionEvent(descriptionTitle: String) {
        val map = createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_DESCRIPTION, descriptionTitle).joinToString(" - "),
                ""
        )
        sendGeneralEvent(map)
    }

    fun sendClickDescriptionEvent(descriptionTitle: String) {
        val map = createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_DESCRIPTION, descriptionTitle, TrackingConstant.SEE_MORE).joinToString(" - "),
                ""
        )
        sendGeneralEvent(map)
    }

    fun sendImpressionProgressBarEvent(dataKey: String, stateColor: String, valueScore: Int) {
        val map = createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_PROGRESS_BAR, "$dataKey $stateColor").joinToString(" - "),
                "$valueScore"
        )
        sendGeneralEvent(map)
    }

    fun sendClickProgressBarEvent(dataKey: String, stateColor: String, valueScore: Int) {
        val map = createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_PROGRESS_BAR, "$dataKey $stateColor", TrackingConstant.SEE_MORE).joinToString(" - "),
                "$valueScore"
        )
        sendGeneralEvent(map)
    }

    fun sendImpressionPostEvent(dataKey: String) {
        val map = createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_POST, dataKey).joinToString(" - "),
                ""
        )
        sendGeneralEvent(map)
    }

    fun sendClickPostSeeMoreEvent(dataKey: String) {
        val map = createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_POST, dataKey, TrackingConstant.SEE_MORE).joinToString(" - "),
                ""
        )
        sendGeneralEvent(map)
    }

    fun sendClickPostItemEvent(dataKey: String, title: String) {
        val map = createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_POST, dataKey, title).joinToString(" - "),
                ""
        )
        sendGeneralEvent(map)
    }

    fun sendClickCarouselCtaEvent(dataKey: String) {
        val map = createMap(
                event = TrackingConstant.CLICK_SELLER_WIDGET,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.CLICK_WIDGET_BANNER, dataKey, TrackingConstant.SEE_MORE).joinToString(" - "),
                label = ""
        )
        sendGeneralEvent(map)
    }

    fun sendClickCarouselItemBannerEvent(dataKey: String, items: List<CarouselItemUiModel>, position: Int) {
        val eventMap = createMap(
                event = TrackingConstant.PROMO_CLICK,
                category = arrayOf(
                        TrackingConstant.SELLER_APP,
                        TrackingConstant.HOME,
                        TrackingConstant.CLICK_WIDGET_BANNER,
                        dataKey,
                        items[position].creativeName
                ).joinToString(" - "),
                action = arrayOf(TrackingConstant.CLICK_WIDGET_BANNER, dataKey).joinToString(" - "),
                label = arrayOf(items[position].appLink, position.toString()).joinToString(" - ")
        )

        val promoClick = mapOf(TrackingConstant.PROMOTIONS to getBannerPromotions(items, position))
        eventMap[TrackingConstant.ECOMMERCE] = mapOf(TrackingConstant.PROMO_CLICK to promoClick)

        sendEnhanceEcommerceEvent(eventMap)
    }

    fun sendImpressionCarouselItemBannerEvent(dataKey: String, items: List<CarouselItemUiModel>, position: Int) {
        val eventMap = createMap(
                event = TrackingConstant.PROMO_VIEW,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.IMPRESSION_WIDGET_BANNER, dataKey).joinToString(" - "),
                label = arrayOf(items[position].appLink, position.toString()).joinToString(" - ")
        )

        val promoView = mapOf(TrackingConstant.PROMOTIONS to getBannerPromotions(items, position))
        eventMap[TrackingConstant.ECOMMERCE] = mapOf(TrackingConstant.PROMO_VIEW to promoView)

        sendEnhanceEcommerceEvent(eventMap)
    }

    private fun getBannerPromotions(items: List<CarouselItemUiModel>, position: Int): List<Map<String, String>> {
        return items.map {
            return@map mapOf(
                    TrackingConstant.ID to "{${it.id}}",
                    TrackingConstant.NAME to TrackingConstant.SELLER_WIDGET,
                    TrackingConstant.CREATIVE to "{${it.creativeName}}",
                    TrackingConstant.CREATIVE_URL to it.featuredMediaURL,
                    TrackingConstant.POSITION to position.toString()
            )
        }
    }

    private fun createMap(event: String, category: String, action: String, label: String): MutableMap<String, Any> {
        return mutableMapOf(
                TrackingConstant.EVENT to event,
                TrackingConstant.EVENT_CATEGORY to category,
                TrackingConstant.EVENT_ACTION to action,
                TrackingConstant.EVENT_LABEL to label
        )
    }

    private fun sendGeneralEvent(eventMap: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun sendEnhanceEcommerceEvent(eventMap: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }
}