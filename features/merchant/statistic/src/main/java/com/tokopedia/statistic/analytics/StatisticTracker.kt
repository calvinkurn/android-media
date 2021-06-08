package com.tokopedia.statistic.analytics

import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface

/**
 * For product insight search table tracker: https://mynakama.tokopedia.com/datatracker/product/requestdetail/1392
 *
 * Created By @ilhamsuaib on 22/07/20
 */

object StatisticTracker {

    fun sendDateFilterEvent(userSession: UserSessionInterface) {
        val mShopStatus = TrackingHelper.getShopStatus(userSession)
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SHOP_INSIGHT,
                TrackingConstant.SHOP_INSIGHT,
                TrackingConstant.CLICK_DATE_FILTER,
                mShopStatus
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendSetDateFilterEvent(filterSubHeader: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SHOP_INSIGHT,
                TrackingConstant.SHOP_INSIGHT,
                TrackingConstant.CLICK_SELECT_ON_DATE_FILTER,
                filterSubHeader
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendCardImpressionEvent(model: CardWidgetUiModel, position: Int) {
        val state = model.data?.state.orEmpty()
        val cardValue = model.data?.value ?: "0"

        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.PROMO_VIEW,
                category = TrackingConstant.SHOP_INSIGHT,
                action = arrayOf(TrackingConstant.IMPRESSION_WIDGET_CARD, "${model.dataKey} $state").joinToString(" - "),
                label = cardValue
        )

        val promoView = mapOf(TrackingConstant.PROMOTIONS to getWidgetPromotions(listOf(model), TrackingConstant.WIDGET_CARD, position))
        eventMap[TrackingConstant.ECOMMERCE] = mapOf(TrackingConstant.PROMO_VIEW to promoView)

        TrackingHelper.sendEnhanceEcommerceEvent(eventMap)
    }

    fun sendClickCardEvent(model: CardWidgetUiModel) {
        val state = model.data?.state.orEmpty()
        val cardValue = model.data?.value ?: "0"

        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SHOP_INSIGHT,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_CARD, "${model.dataKey} $state").joinToString(" - "),
                cardValue
        )

        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionLineGraphEvent(model: LineGraphWidgetUiModel, position: Int) {
        val eventMap = TrackingHelper.createMap(
                TrackingConstant.PROMO_VIEW,
                TrackingConstant.SHOP_INSIGHT,
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_LINE_GRAPH, model.dataKey).joinToString(" - "),
                model.data?.header.orEmpty()
        )

        val promoView = mapOf(TrackingConstant.PROMOTIONS to getWidgetPromotions(listOf(model), TrackingConstant.WIDGET_TREND_LINE, position))
        eventMap[TrackingConstant.ECOMMERCE] = mapOf(TrackingConstant.PROMO_VIEW to promoView)

        TrackingHelper.sendEnhanceEcommerceEvent(eventMap)
    }

    fun sendEmptyStateCtaClickLineGraphEvent(model: LineGraphWidgetUiModel) {
        val isEmpty = model.data?.list?.all { it.yVal == 0f } == true
        val emptyStatus = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val dataKey = model.dataKey
        val cardValue = model.data?.header.orEmpty()

        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_HOMEPAGE,
                TrackingConstant.SELLER_APP_STATISTIC,
                "${TrackingConstant.IMPRESSION_WIDGET_LINE_GRAPH} - $dataKey",
                "$emptyStatus - $cardValue"
        )
        map[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        map[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIASELLER

        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickLineGraphEvent(dataKey: String, chartValue: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SHOP_INSIGHT,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_LINE_GRAPH, dataKey, TrackingConstant.SEE_MORE).joinToString(" - "),
                chartValue
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionDescriptionEvent(descriptionTitle: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.SHOP_INSIGHT,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.IMPRESSION_WIDGET_DESCRIPTION, descriptionTitle).joinToString(" - "),
                ""
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickDescriptionEvent(descriptionTitle: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SHOP_INSIGHT,
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
                TrackingConstant.CLICK_SHOP_INSIGHT,
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
                TrackingConstant.CLICK_SHOP_INSIGHT,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_POST, dataKey, TrackingConstant.SEE_MORE).joinToString(" - "),
                ""
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickPostItemEvent(dataKey: String, title: String) {
        val map = TrackingHelper.createMap(
                TrackingConstant.CLICK_SHOP_INSIGHT,
                arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(" - "),
                arrayOf(TrackingConstant.CLICK_WIDGET_POST, dataKey, title).joinToString(" - "),
                ""
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickCarouselCtaEvent(dataKey: String) {
        val map = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_SHOP_INSIGHT,
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

    fun sendTableImpressionEvent(model: TableWidgetUiModel, position: Int, slideNumber: Int, isSlideEmpty: Boolean) {
        val state = if (isSlideEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.PROMO_VIEW,
                category = TrackingConstant.SHOP_INSIGHT,
                action = arrayOf(TrackingConstant.IMPRESSION_WIDGET_SIMPLE_TABLE, model.dataKey).joinToString(" - "),
                label = "$state - $slideNumber"
        )

        val promoView = mapOf(TrackingConstant.PROMOTIONS to getWidgetPromotions(listOf(model), TrackingConstant.WIDGET_SIMPLE_TABLE, position))
        eventMap[TrackingConstant.ECOMMERCE] = mapOf(TrackingConstant.PROMO_VIEW to promoView)

        TrackingHelper.sendEnhanceEcommerceEvent(eventMap)
    }

    fun sendTableSlideEvent(categoryPage: String, currentPage: Int, totalPage: Int) {
        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_PRODUCT_INSIGHT,
                category = categoryPage,
                action = TrackingConstant.SLIDE_TABLE_WIDGET,
                label = arrayOf(currentPage, totalPage).joinToString(" - ")
        ).apply {
            this[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
            this[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIASELLER
        }

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTableFilterImpressionEvent(categoryPage: String) {
        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.VIEW_PRODUCT_INSIGHT_IRIS,
                category = categoryPage,
                action = TrackingConstant.IMPRESSION_WIDGET_TABLE_FILTER,
                label = ""
        ).apply {
            this[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
            this[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIASELLER
        }

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTableFilterClickEvent(categoryPage: String, filterOption: String) {
        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_PRODUCT_INSIGHT,
                category = categoryPage,
                action = TrackingConstant.CLICK_TABLE_WIDGET_FILTER,
                label = filterOption
        ).apply {
            this[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
            this[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIASELLER
        }

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendPieChartImpressionEvent(model: PieChartWidgetUiModel, position: Int) {
        val value = model.data?.data?.summary?.value?.toString().orEmpty()

        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.PROMO_VIEW,
                category = TrackingConstant.SHOP_INSIGHT,
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
                category = TrackingConstant.SHOP_INSIGHT,
                action = arrayOf(TrackingConstant.IMPRESSION_WIDGET_BAR_CHART, model.dataKey).joinToString(" - "),
                label = "$state - $value"
        )

        val promoView = mapOf(TrackingConstant.PROMOTIONS to getWidgetPromotions(listOf(model), TrackingConstant.WIDGET_BAR_CHART, position))
        eventMap[TrackingConstant.ECOMMERCE] = mapOf(TrackingConstant.PROMO_VIEW to promoView)

        TrackingHelper.sendEnhanceEcommerceEvent(eventMap)
    }

    fun sendSectionTooltipClickEvent(sectionTitle: String) {
        val eventMap = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_SHOP_INSIGHT,
                category = TrackingConstant.SHOP_INSIGHT,
                action = TrackingConstant.CLICK_INFO_ICON,
                label = sectionTitle
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendPageTabImpressionEvent(userId: String, tabName: String) {
        val eventMap = createEventMap(
                event = TrackingConstant.VIEW_STATISTIC_IRIS,
                action = TrackingConstant.IMPRESSION_MENU_TAB,
                label = tabName,
                userId = userId
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendPageTabClickEvent(userId: String, tabName: String) {
        val eventMap = createEventMap(
                event = TrackingConstant.CLICK_STATISTIC,
                action = TrackingConstant.CLICK_MENU_TAB,
                label = tabName,
                userId = userId
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendCalendarImpressionEvent(userId: String, tabName: String, chosenPeriod: String) {
        val eventMap = createEventMap(
                event = TrackingConstant.VIEW_STATISTIC_IRIS,
                action = String.format(TrackingConstant.IMPRESSION_CALENDAR, tabName),
                label = chosenPeriod,
                userId = userId
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendCalendarClickEvent(userId: String, tabName: String, chosenPeriod: String) {
        val eventMap = createEventMap(
                event = TrackingConstant.CLICK_STATISTIC,
                action = String.format(TrackingConstant.CLICK_MENU_CALENDAR, tabName),
                label = chosenPeriod,
                userId = userId
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendThreeDotsImpressionEvent(userId: String) {
        val eventMap = createEventMap(
                event = TrackingConstant.VIEW_STATISTIC_IRIS,
                action = TrackingConstant.IMPRESSION_3_DOT,
                label = "",
                userId = userId
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendThreeDotsClickEvent(userId: String) {
        val eventMap = createEventMap(
                event = TrackingConstant.CLICK_STATISTIC,
                action = TrackingConstant.CLICK_MENU_3_DOT,
                label = "",
                userId = userId
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendActionMenuBottomSheetImpressionEvent(userId: String, tabName: String, label: String) {
        val eventMap = createEventMap(
                event = TrackingConstant.VIEW_STATISTIC_IRIS,
                action = String.format(TrackingConstant.IMPRESSION_MENU_LAINNYA, tabName),
                label = label,
                userId = userId
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendActionMenuBottomSheetClickEvent(userId: String, tabName: String, label: String) {
        val eventMap = createEventMap(
                event = TrackingConstant.CLICK_STATISTIC,
                action = String.format(TrackingConstant.CLICK_MENU_MENU_LAINNYA, tabName),
                label = label,
                userId = userId
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    private fun createEventMap(event: String, action: String, label: String, userId: String): MutableMap<String, Any> {
        return TrackingHelper.createMap(
                event = event,
                category = TrackingConstant.SELLER_APP_STATISTIC,
                action = action,
                label = label
        ).apply {
            this[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
            this[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIASELLER
            this[TrackingConstant.USER_ID] = userId
        }
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