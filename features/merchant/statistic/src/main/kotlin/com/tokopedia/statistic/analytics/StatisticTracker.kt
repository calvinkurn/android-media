package com.tokopedia.statistic.analytics

import com.tokopedia.sellerhomecommon.presentation.model.CarouselItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TablePageUiModel
import com.tokopedia.track.TrackApp

/**
 * Created By @ilhamsuaib on 22/07/20
 */

object StatisticTracker {

    fun sendCardImpressionEvent(dataKey: String, state: String, cardValue: String) {
        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.PROMO_VIEW,
                category = TrackingConstant.SHOP_INSIGHT,
                action = arrayOf(TrackingConstant.IMPRESSION_WIDGET_CARD, "$dataKey $state").joinToString(" - "),
                label = cardValue
        )

        /*val promoView = mapOf(TrackingConstant.PROMOTIONS to getCardPromotions(cards, position))
        eventMap[TrackingConstant.ECOMMERCE] = mapOf(TrackingConstant.PROMO_VIEW to promoView)

        TrackingHelper.sendEnhanceEcommerceEvent(eventMap)*/
    }

    fun sendClickCardEvent(dataKey: String, state: String, cardValue: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_CARD, "$dataKey $state").joinToString(" - "),
                cardValue
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionLineGraphEvent(dataKey: String, cardValue: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_LINE_GRAPH, dataKey).joinToString(" - "),
                cardValue
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickLineGraphEvent(dataKey: String, chartValue: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_LINE_GRAPH, dataKey, TrackingConstant.SEE_MORE).joinToString(" - "),
                chartValue
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionDescriptionEvent(descriptionTitle: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_DESCRIPTION, descriptionTitle).joinToString(" - "),
                ""
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickDescriptionEvent(descriptionTitle: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_DESCRIPTION, descriptionTitle, TrackingConstant.SEE_MORE).joinToString(" - "),
                ""
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionProgressBarEvent(dataKey: String, stateColor: String, valueScore: Int) {
        val map = TrackingHelper.createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_PROGRESS_BAR, "$dataKey $stateColor").joinToString(" - "),
                "$valueScore"
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickProgressBarEvent(dataKey: String, stateColor: String, valueScore: Int) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_PROGRESS_BAR, "$dataKey $stateColor", TrackingConstant.SEE_MORE).joinToString(" - "),
                "$valueScore"
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionPostEvent(dataKey: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.VIEW_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_POST, dataKey).joinToString(" - "),
                ""
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickPostSeeMoreEvent(dataKey: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_POST, dataKey, TrackingConstant.SEE_MORE).joinToString(" - "),
                ""
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickPostItemEvent(dataKey: String, title: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_POST, dataKey, title).joinToString(" - "),
                ""
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickCarouselCtaEvent(dataKey: String) {
        val map = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_SELLER_WIDGET,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
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
                        TrackingConstant.SHOP_INSIGHT,
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
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                action = arrayOf(TrackingConstant.IMPRESSION_WIDGET_BANNER, dataKey).joinToString(" - "),
                label = arrayOf(items[position].appLink, position.toString()).joinToString(" - ")
        )

        val promoView = mapOf(TrackingConstant.PROMOTIONS to getBannerPromotions(items, position))
        eventMap[TrackingConstant.ECOMMERCE] = mapOf(TrackingConstant.PROMO_VIEW to promoView)

        TrackingHelper.sendEnhanceEcommerceEvent(eventMap)
    }

    fun sendTableImpressionEvent(dataKey: String, eventLabel: String, tablePages: List<TablePageUiModel>, position: Int) {
        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.PROMO_VIEW,
                category = TrackingConstant.SHOP_INSIGHT,
                action = arrayOf(TrackingConstant.IMPRESSION_WIDGET_TABLE, dataKey).joinToString(" - "),
                label = eventLabel
        )

        val promoView = mapOf(TrackingConstant.PROMOTIONS to getTablePromotions(tablePages, position))
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

    private fun getTablePromotions(tablePages: List<TablePageUiModel>, position: Int): List<Map<String, String>> {
        return tablePages.map {
            return@map mapOf(
                    TrackingConstant.ID to "{}",
                    TrackingConstant.NAME to TrackingConstant.WIDGET_SIMPLE_TABLE,
                    TrackingConstant.CREATIVE to "{}",
                    TrackingConstant.CREATIVE_URL to "",
                    TrackingConstant.POSITION to position.toString()
            )
        }
    }

    private fun getCardPromotions(cards: List<String>, position: Int): List<Map<String, String>> {
        return cards.map {
            return@map mapOf(
                    TrackingConstant.ID to "{}",
                    TrackingConstant.NAME to TrackingConstant.WIDGET_SIMPLE_CARD,
                    TrackingConstant.CREATIVE to "{}",
                    TrackingConstant.CREATIVE_URL to "",
                    TrackingConstant.POSITION to position.toString()
            )
        }
    }
}