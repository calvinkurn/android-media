package com.tokopedia.sellerhome.analytic

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.track.TrackApp

/**
 * Created By @ilhamsuaib on 2020-02-11
 */

/**
 * Seller Home Revamp Tracker
 * Data Layer : https://docs.google.com/spreadsheets/d/13WEeOReKimxp9ugeVMew6T-ma9gNQHaWJxYN6DK-1x0/edit?ts=5e395338#gid=389108416
 * Data Tracker for Recommendation Widget : https://mynakama.tokopedia.com/datatracker/requestdetail/781
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

    fun sendImpressionPostEvent(model: PostListWidgetUiModel, userId: String) {
        val isEmpty = model.data?.items.isNullOrEmpty()
        val filterType = model.postFilter.find { it.isSelected }?.value.orEmpty()

        val map = TrackingHelper.createMap(
                TrackingConstant.VIEW_SELLER_HOMEPAGE_IRIS,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_POST, model.dataKey).joinToString(" - "),
                "${if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY}${" - $filterType".takeIf { filterType.isNotBlank() } ?: ""}"
        )
        map[TrackingConstant.USER_ID] = userId
        map[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        map[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIA_SELLER

        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickPostSeeMoreEvent(model: PostListWidgetUiModel, userId: String) {
        val isEmpty = model.data?.items.isNullOrEmpty()
        val filterType = model.postFilter.find { it.isSelected }?.value.orEmpty()

        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_HOMEPAGE,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_POST, model.dataKey, TrackingConstant.SEE_MORE).joinToString(" - "),
                "${if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY}${" - $filterType".takeIf { filterType.isNotBlank() } ?: ""}"
        )
        map[TrackingConstant.USER_ID] = userId
        map[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        map[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIA_SELLER

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

    fun sendPostListFilterClick(model: PostListWidgetUiModel, userId: String) {
        val isEmpty = model.data?.items.isNullOrEmpty()
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SELLER_WIDGET,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_POST, model.dataKey, TrackingConstant.FILTER).joinToString(" - "),
                label = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        )
        map[TrackingConstant.USER_ID] = userId

        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendPostEmptyStateCtaClick(model: PostListWidgetUiModel, userId: String) {
        val isEmpty = model.data?.items.isNullOrEmpty()

        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_HOMEPAGE,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = "${arrayOf(TrackingConstant.CLICK_WIDGET_POST, model.dataKey).joinToString(" - ")} ${TrackingConstant.EMPTY_STATE}",
                label = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        )
        eventMap[TrackingConstant.USER_ID] = userId
        eventMap[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        eventMap[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIA_SELLER

        TrackingHelper.sendGeneralEvent(eventMap)
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

    fun sendTableClickHyperlinkEvent(dataKey: String, url: String, isEmpty: Boolean, userId: String) {
        val state = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val map = mutableMapOf<String, Any>(
                TrackingConstant.EVENT to TrackingConstant.CLICK_SELLER_WIDGET,
                TrackingConstant.EVENT_CATEGORY to arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                TrackingConstant.EVENT_ACTION to arrayOf(TrackingConstant.CLICK_WIDGET_ADVANCE_TABLE, dataKey).joinToString(" - "),
                TrackingConstant.EVENT_LABEL to arrayOf(state, url).joinToString(" - "),
                TrackingConstant.BUSINESS_UNIT to TrackingConstant.PHYSICAL_GOODS,
                TrackingConstant.CURRENT_SITE to TrackingConstant.TOKOPEDIA_SELLER,
                TrackingConstant.USER_ID to userId)
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendTableEmptyStateCtaClick(model: TableWidgetUiModel, userId: String) {
        val isEmpty = model.data?.dataSet.isNullOrEmpty()

        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_HOMEPAGE,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = "${arrayOf(TrackingConstant.CLICK_WIDGET_SIMPLE_TABLE, model.dataKey).joinToString(" - ")} ${TrackingConstant.EMPTY_STATE}",
                label = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        )
        eventMap[TrackingConstant.USER_ID] = userId
        eventMap[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        eventMap[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIA_SELLER

        TrackingHelper.sendGeneralEvent(eventMap)
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

    fun sendAnnouncementImpressionEvent(model: AnnouncementWidgetUiModel, userId: String) {
        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.VIEW_SELLER_WIDGET_IRIS,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.IMPRESSION_WIDGET_ANNOUNCEMENT, model.dataKey).joinToString(" - "),
                label = TrackingConstant.EMPTY
        )
        eventMap[TrackingConstant.USER_ID] = userId

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendAnnouncementClickEvent(model: AnnouncementWidgetUiModel, userId: String) {
        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_SELLER_WIDGET,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.CLICK_WIDGET_MULTI_ANNOUNCEMENT, model.dataKey).joinToString(" - "),
                label = TrackingConstant.EMPTY
        )
        eventMap[TrackingConstant.USER_ID] = userId

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMultiLineGraphMetricClick(model: MultiLineGraphWidgetUiModel, metric: MultiLineMetricUiModel, userId: String) {
        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_SELLER_WIDGET,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.CLICK_WIDGET_MULTI_LINE_GRAPH, model.dataKey).joinToString(" - "),
                label = if (metric.isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        )
        eventMap[TrackingConstant.USER_ID] = userId

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMultiLineGraphCtaClick(model: MultiLineGraphWidgetUiModel, userId: String) {
        val isEmpty = model.data?.metrics?.getOrNull(0)?.isEmpty ?: true
        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_SELLER_WIDGET,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.CLICK_WIDGET_MULTI_LINE_GRAPH, model.dataKey, TrackingConstant.SEE_MORE).joinToString(" - "),
                label = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        )
        eventMap[TrackingConstant.USER_ID] = userId

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMultiLineGraphImpressionEvent(model: MultiLineGraphWidgetUiModel, userId: String) {
        val isEmpty = model.data?.metrics?.getOrNull(0)?.isEmpty ?: true

        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.VIEW_SELLER_WIDGET_IRIS,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.IMPRESSION_WIDGET_MULTI_LINE_GRAPH, model.dataKey).joinToString(" - "),
                label = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        )
        eventMap[TrackingConstant.USER_ID] = userId

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMultiLineGraphEmptyStateCtaClick(model: MultiLineGraphWidgetUiModel, userId: String) {
        val isEmpty = model.data?.metrics?.getOrNull(0)?.isEmpty ?: true

        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_HOMEPAGE,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = "${arrayOf(TrackingConstant.CLICK_WIDGET_MULTI_LINE_GRAPH, model.dataKey).joinToString(" - ")} ${TrackingConstant.EMPTY_STATE}",
                label = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        )
        eventMap[TrackingConstant.USER_ID] = userId
        eventMap[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        eventMap[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIA_SELLER

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendRecommendationCtaClickEvent(element: RecommendationWidgetUiModel) {
        val scoreLevel = element.data?.progressLevel?.bar?.value.orZero()
        val numOfNegativeRecommendation = getNumberOfRecommendationByType(element.data?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_NEGATIVE)
        val numOfPositiveRecommendation = getNumberOfRecommendationByType(element.data?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_POSITIVE)
        val numOfNoDataRecommendation = getNumberOfRecommendationByType(element.data?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_NO_DATA)
        val tickerStatus = if (element.data?.ticker?.text.isNullOrBlank()) "off" else "on"
        val tickerLabel = "ticker $tickerStatus"
        val eventLabel = arrayOf(scoreLevel, numOfNegativeRecommendation, numOfPositiveRecommendation,
                numOfNoDataRecommendation, tickerLabel).joinToString(" - ")

        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_SELLER_DASHBOARD,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.CLICK_RECOMMENDATION_WIDGET, element.dataKey, TrackingConstant.SEE_MORE).joinToString(" - "),
                label = eventLabel
        )
        eventMap[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        eventMap[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIA_SELLER

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendRecommendationImpressionEvent(element: RecommendationWidgetUiModel) {
        val scoreLevel = element.data?.progressLevel?.bar?.value.orZero()
        val numOfNegativeRecommendation = getNumberOfRecommendationByType(element.data?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_NEGATIVE)
        val numOfPositiveRecommendation = getNumberOfRecommendationByType(element.data?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_POSITIVE)
        val numOfNoDataRecommendation = getNumberOfRecommendationByType(element.data?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_NO_DATA)
        val tickerStatus = if (element.data?.ticker?.text.isNullOrBlank()) "off" else "on"
        val tickerLabel = "ticker $tickerStatus"
        val eventLabel = arrayOf(scoreLevel, numOfNegativeRecommendation, numOfPositiveRecommendation,
                numOfNoDataRecommendation, tickerLabel).joinToString(" - ")

        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.VIEW_SELLER_DASHBOARD,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.IMPRESSION_RECOMMENDATION_WIDGET, element.dataKey).joinToString(" - "),
                label = eventLabel
        )
        eventMap[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        eventMap[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIA_SELLER

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendRecommendationItemClickEvent(dataKey: String, item: RecommendationItemUiModel) {
        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_SELLER_DASHBOARD,
                category = arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.HOME).joinToString(" - "),
                action = arrayOf(TrackingConstant.CLICK_RECOMMENDATION_WIDGET, dataKey, TrackingConstant.CLICK_RECOMMENDATION).joinToString(" - "),
                label = arrayOf(item.type, item.text).joinToString(" - ")
        )
        eventMap[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        eventMap[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIA_SELLER

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendHomeTickerCtaClickEvent(shopType: String) {
        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_SHOP_SCORE,
                category = TrackingConstant.ACTION_CLICK_LEARN_MORE,
                action = TrackingConstant.CATEGORY_COMMUNICATION_PERIOD_HOME,
                label = shopType
        )
        eventMap[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        eventMap[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIA_SELLER

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendHomeTickerImpressionEvent(shopType: String) {
        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.VIEW_SHOP_SCORE_IRIS,
                category = TrackingConstant.IMPRESS_LEARN_MORE,
                action = TrackingConstant.CATEGORY_COMMUNICATION_PERIOD_HOME,
                label = shopType
        )
        eventMap[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        eventMap[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIA_SELLER

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    private fun getNumberOfRecommendationByType(recommendations: List<RecommendationItemUiModel>?, type: String): Int {
        return recommendations?.filter { it.type == type }?.size.orZero()
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