package com.tokopedia.sellerhome.analytic

import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.track.TrackApp

/**
 * Created By @ilhamsuaib on 2020-02-11
 */

/**
 * Seller Home Revamp Tracker
 * Data Layer : https://docs.google.com/spreadsheets/d/13WEeOReKimxp9ugeVMew6T-ma9gNQHaWJxYN6DK-1x0/edit?ts=5e395338#gid=389108416
 * */

object SellerHomeTracking {

    fun sendImpressionCardEvent(dataKey: String, state: String, cardValue: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_CARD, "$dataKey $state").joinToString(" - "),
                cardValue
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickCardEvent(dataKey: String, state: String, cardValue: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_CARD, "$dataKey $state").joinToString(" - "),
                cardValue
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionLineGraphEvent(dataKey: String, cardValue: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_LINE_GRAPH, dataKey).joinToString(" - "),
                cardValue
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickLineGraphEvent(dataKey: String, cardValue: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_LINE_GRAPH, dataKey, TrackingConstant.SEE_MORE).joinToString(" - "),
                cardValue
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionDescriptionEvent(descriptionTitle: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_DESCRIPTION, descriptionTitle).joinToString(" - "),
                ""
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickDescriptionEvent(descriptionTitle: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_DESCRIPTION, descriptionTitle, TrackingConstant.SEE_MORE).joinToString(" - "),
                ""
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionProgressBarEvent(dataKey: String, stateColor: String, valueScore: Int) {
        val map = TrackingHelper.createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_PROGRESS_BAR, "$dataKey $stateColor").joinToString(" - "),
                "$valueScore"
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickProgressBarEvent(dataKey: String, stateColor: String, valueScore: Int) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_PROGRESS_BAR, "$dataKey $stateColor", TrackingConstant.SEE_MORE).joinToString(" - "),
                "$valueScore"
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionPostEvent(dataKey: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_POST, dataKey).joinToString(" - "),
                ""
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickPostSeeMoreEvent(dataKey: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_POST, dataKey, TrackingConstant.SEE_MORE).joinToString(" - "),
                ""
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickPostItemEvent(dataKey: String, title: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_POST, dataKey, title).joinToString(" - "),
                ""
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickCarouselCtaEvent(dataKey: String) {
        val map = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_SELLER_WIDGET,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.CLICK_WIDGET_BANNER, dataKey, TrackingConstant.SEE_MORE).joinToString(" - "),
                label = ""
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickCarouselItemBannerEvent(dataKey: String, items: List<CarouselItemUiModel>, position: Int) {
        val eventMap = TrackingHelper.createMap(
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

        TrackingHelper.sendEnhanceEcommerceEvent(eventMap)
    }

    fun sendImpressionCarouselItemBannerEvent(dataKey: String, items: List<CarouselItemUiModel>, position: Int) {
        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.PROMO_VIEW,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.IMPRESSION_WIDGET_BANNER, dataKey).joinToString(" - "),
                label = arrayOf(items[position].appLink, position.toString()).joinToString(" - ")
        )

        val promoView = mapOf(TrackingConstant.PROMOTIONS to getBannerPromotions(items, position))
        eventMap[TrackingConstant.ECOMMERCE] = mapOf(TrackingConstant.PROMO_VIEW to promoView)

        TrackingHelper.sendEnhanceEcommerceEvent(eventMap)
    }

    fun sendTableImpressionEvent(model: TableWidgetUiModel, position: Int, slideNumber: Int, isSlideEmpty: Boolean) {
        val state = if (isSlideEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.PROMO_VIEW,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.IMPRESSION_WIDGET_TABLE, model.dataKey).joinToString(" - "),
                label = "$state - $slideNumber"
        )

        val promoView = mapOf(TrackingConstant.PROMOTIONS to getWidgetPromotions(listOf(model), TrackingConstant.WIDGET_SIMPLE_TABLE, position))
        eventMap[TrackingConstant.ECOMMERCE] = mapOf(TrackingConstant.PROMO_VIEW to promoView)

        TrackingHelper.sendEnhanceEcommerceEvent(eventMap)
    }

    fun sendPieChartImpressionEvent(model: PieChartWidgetUiModel, position: Int) {
        val value = model.data?.data?.summary?.value?.toString().orEmpty()

        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.PROMO_VIEW,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.IMPRESSION_WIDGET_PIE_CHART, model.dataKey).joinToString(" - "),
                label = value
        )

        val promoView = mapOf(TrackingConstant.PROMOTIONS to getWidgetPromotions(listOf(model), TrackingConstant.WIDGET_PIE_CHART, position))
        eventMap[TrackingConstant.ECOMMERCE] = mapOf(TrackingConstant.PROMO_VIEW to promoView)

        TrackingHelper.sendEnhanceEcommerceEvent(eventMap)
    }

    fun sendBarChartImpressionEvent(model: BarChartWidgetUiModel, position: Int) {
        val isEmpty = model.data?.chartData?.metrics.isNullOrEmpty()
        val value = model.data?.chartData?.summary?.value?.toString().orEmpty()
        val state = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.PROMO_VIEW,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.IMPRESSION_WIDGET_BAR_CHART, model.dataKey).joinToString(" - "),
                label = "$state - $value"
        )

        val promoView = mapOf(TrackingConstant.PROMOTIONS to getWidgetPromotions(listOf(model), TrackingConstant.WIDGET_BAR_CHART, position))
        eventMap[TrackingConstant.ECOMMERCE] = mapOf(TrackingConstant.PROMO_VIEW to promoView)

        TrackingHelper.sendEnhanceEcommerceEvent(eventMap)
    }

    fun sendScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
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

    private fun getWidgetPromotions(cards: List<BaseWidgetUiModel<*>>, name: String, position: Int): List<Map<String, String>> {
        return cards.map {
            return@map mapOf(
                    TrackingConstant.ID to it.dataKey,
                    TrackingConstant.NAME to name,
                    TrackingConstant.CREATIVE to TrackingConstant.NONE,
                    TrackingConstant.CREATIVE_URL to TrackingConstant.NONE,
                    TrackingConstant.CATEGORY to TrackingConstant.NONE,
                    TrackingConstant.PROMO_ID to TrackingConstant.NONE,
                    TrackingConstant.PROMO_CODE to TrackingConstant.NONE,
                    TrackingConstant.POSITION to position.toString()
            )
        }
    }
}