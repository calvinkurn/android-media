package com.tokopedia.statistic.analytics

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerhomecommon.presentation.model.BarChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.common.Const
import com.tokopedia.statistic.view.model.StatisticPageUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import java.util.*

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

    fun sendSetDateFilterEvent(pageSource: String, type: String, startDate: Date?, endDate: Date?) {
        val startDateFmt = if (startDate == null) {
            String.EMPTY
        } else {
            DateTimeUtil.format(startDate.time, DateTimeUtil.FORMAT_DD_MMM_YYYY)
        }
        val endDateFmt = if (endDate == null) {
            String.EMPTY
        } else {
            DateTimeUtil.format(endDate.time, DateTimeUtil.FORMAT_DD_MMM_YYYY)
        }
        val map = createEventMap(
            event = TrackingConstant.CLICK_PG,
            action = TrackingConstant.CLICK_SELECT_ON_DATE_FILTER,
            category = getEventCategory(pageSource),
            label = String.format(
                TrackingConstant.DATE_FILTER_FORMAT,
                type,
                startDateFmt,
                endDateFmt
            )
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendCardImpressionEvent(pageSource: String, model: CardWidgetUiModel) {
        val state = model.data?.state?.name.orEmpty()
        val cardValue = model.data?.value ?: Int.ZERO.toString()
        val description = model.data?.description.orEmpty()
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            category = getEventCategory(pageSource),
            action = TrackingConstant.IMPRESSION_WIDGET_CARD,
            label = arrayOf(
                model.dataKey,
                state,
                cardValue.parseAsHtml(),
                description.parseAsHtml()
            ).joinToString(TrackingConstant.SEPARATOR)
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendClickCardEvent(model: CardWidgetUiModel) {
        val state = model.data?.state?.name.orEmpty()
        val cardValue = model.data?.value ?: "0"

        val map = TrackingHelper.createMap(
            TrackingConstant.CLICK_SHOP_INSIGHT,
            arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(
                TrackingConstant.SEPARATOR
            ),
            arrayOf(
                TrackingConstant.CLICK_WIDGET_CARD,
                "${model.dataKey} $state"
            ).joinToString(TrackingConstant.SEPARATOR),
            cardValue.parseAsHtml().toString()
        )

        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionLineGraphEvent(pageSource: String, model: LineGraphWidgetUiModel) {
        val state = if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val value = model.data?.header.orEmpty()
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            action = TrackingConstant.IMPRESSION_WIDGET_LINE_GRAPH,
            category = getEventCategory(pageSource),
            label = arrayOf(
                model.dataKey, state, value
            ).joinToString(TrackingConstant.SEPARATOR)
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendEmptyStateCtaClickLineGraphEvent(model: LineGraphWidgetUiModel) {
        val state = if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val dataKey = model.dataKey
        val cardValue = model.data?.header.orEmpty()

        val map = TrackingHelper.createMap(
            TrackingConstant.CLICK_HOMEPAGE,
            TrackingConstant.SELLER_APP_STATISTIC,
            "${TrackingConstant.CLICK_WIDGET_LINE_GRAPH} - $dataKey",
            "$state - $cardValue"
        )

        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickLineGraphEvent(dataKey: String, chartValue: String) {
        val map = TrackingHelper.createMap(
            TrackingConstant.CLICK_SHOP_INSIGHT,
            arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(
                TrackingConstant.SEPARATOR
            ),
            arrayOf(
                TrackingConstant.CLICK_WIDGET_LINE_GRAPH,
                dataKey,
                TrackingConstant.SEE_MORE
            ).joinToString(TrackingConstant.SEPARATOR),
            chartValue
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionDescriptionEvent(descriptionTitle: String) {
        val map = TrackingHelper.createMap(
            TrackingConstant.SHOP_INSIGHT,
            arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(
                TrackingConstant.SEPARATOR
            ),
            arrayOf(
                TrackingConstant.IMPRESSION_WIDGET_DESCRIPTION,
                descriptionTitle
            ).joinToString(TrackingConstant.SEPARATOR),
            String.EMPTY
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickDescriptionEvent(descriptionTitle: String) {
        val map = TrackingHelper.createMap(
            TrackingConstant.CLICK_SHOP_INSIGHT,
            arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(
                TrackingConstant.SEPARATOR
            ),
            arrayOf(
                TrackingConstant.CLICK_WIDGET_DESCRIPTION,
                descriptionTitle,
                TrackingConstant.SEE_MORE
            ).joinToString(TrackingConstant.SEPARATOR),
            String.EMPTY
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionProgressBarEvent(dataKey: String, stateColor: String, valueScore: Long) {
        val map = TrackingHelper.createMap(
            TrackingConstant.VIEW_SELLER_WIDGET,
            arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(
                TrackingConstant.SEPARATOR
            ),
            arrayOf(
                TrackingConstant.IMPRESSION_WIDGET_PROGRESS_BAR,
                "$dataKey $stateColor"
            ).joinToString(TrackingConstant.SEPARATOR),
            valueScore.toString()
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickProgressBarEvent(dataKey: String, stateColor: String, valueScore: Long) {
        val map = TrackingHelper.createMap(
            TrackingConstant.CLICK_SHOP_INSIGHT,
            arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(
                TrackingConstant.SEPARATOR
            ),
            arrayOf(
                TrackingConstant.CLICK_WIDGET_PROGRESS_BAR,
                "$dataKey $stateColor",
                TrackingConstant.SEE_MORE
            ).joinToString(TrackingConstant.SEPARATOR),
            "$valueScore"
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionPostEvent(dataKey: String) {
        val map = TrackingHelper.createMap(
            TrackingConstant.VIEW_SELLER_WIDGET,
            arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(
                TrackingConstant.SEPARATOR
            ),
            arrayOf(
                TrackingConstant.IMPRESSION_WIDGET_POST,
                dataKey
            ).joinToString(TrackingConstant.SEPARATOR),
            String.EMPTY
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickPostSeeMoreEvent(dataKey: String) {
        val map = TrackingHelper.createMap(
            TrackingConstant.CLICK_SHOP_INSIGHT,
            arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(
                TrackingConstant.SEPARATOR
            ),
            arrayOf(
                TrackingConstant.CLICK_WIDGET_POST,
                dataKey,
                TrackingConstant.SEE_MORE
            ).joinToString(TrackingConstant.SEPARATOR),
            String.EMPTY
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickPostItemEvent(dataKey: String, title: String) {
        val map = TrackingHelper.createMap(
            TrackingConstant.CLICK_SHOP_INSIGHT,
            arrayOf(TrackingConstant.SELLER_APP, TrackingConstant.SHOP_INSIGHT).joinToString(
                TrackingConstant.SEPARATOR
            ),
            arrayOf(TrackingConstant.CLICK_WIDGET_POST, dataKey, title).joinToString(
                TrackingConstant.SEPARATOR
            ),
            String.EMPTY
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendTableImpressionEvent(
        pageSource: String,
        dataKey: String,
        isSlideEmpty: Boolean
    ) {
        val state = if (isSlideEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            category = getEventCategory(pageSource),
            action = TrackingConstant.IMPRESSION_TABLE_WIDGET,
            label = arrayOf(dataKey, state).joinToString(TrackingConstant.SEPARATOR)
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTableOnSwipeEvent(
        pageSource: String,
        slidePosition: Int,
        maxSlidePosition: Int
    ) {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            category = getEventCategory(pageSource),
            action = TrackingConstant.CLICK_TABLE_SWIPE,
            label = arrayOf(slidePosition.toString(), maxSlidePosition.toString())
                .joinToString(TrackingConstant.SEPARATOR)
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTableFilterImpressionEvent(categoryPage: String) {
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PRODUCT_INSIGHT_IRIS,
            category = categoryPage,
            action = TrackingConstant.IMPRESSION_WIDGET_TABLE_FILTER,
            label = String.EMPTY
        ).apply {
            this[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
            this[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIASELLER
        }

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTableFilterClickEvent(
        pageSource: String,
        dataKey: String,
        filterOption: String,
        isEmpty: Boolean
    ) {
        val emptyLabel = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            category = getEventCategory(pageSource),
            action = TrackingConstant.CLICK_TABLE_WIDGET_FILTER,
            label = arrayOf(dataKey, emptyLabel, filterOption)
                .joinToString(TrackingConstant.SEPARATOR)
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendPieChartImpressionEvent(pageSource: String, model: PieChartWidgetUiModel) {
        val value = model.data?.data?.summary?.value?.toString().orEmpty()
        val state = if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            category = getEventCategory(pageSource),
            action = TrackingConstant.IMPRESSION_WIDGET_PIE_CHART,
            label = arrayOf(
                model.dataKey, state, value
            ).joinToString(TrackingConstant.SEPARATOR)
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendPieChartEmptyStateCtaClickEvent(model: PieChartWidgetUiModel) {
        val value = model.data?.data?.summary?.value?.toString().orEmpty()
        val state = if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_STATISTIC,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_PIE_CHART,
                model.dataKey
            ).joinToString(TrackingConstant.SEPARATOR),
            label = "$state - $value"
        )
        eventMap[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        eventMap[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIASELLER

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendBarChartImpressionEvent(pageSource: String, model: BarChartWidgetUiModel) {
        val value = model.data?.chartData?.summary?.value?.toString().orEmpty()
        val state = if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            category = getEventCategory(pageSource),
            action = TrackingConstant.IMPRESSION_WIDGET_BAR_CHART,
            label = arrayOf(
                model.dataKey, state, value
            ).joinToString(TrackingConstant.SEPARATOR)
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendBarChartEmptyStateCtaClickEvent(model: BarChartWidgetUiModel) {
        val value = model.data?.chartData?.summary?.value?.toString().orEmpty()
        val state = if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_STATISTIC,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_BAR_CHART,
                model.dataKey
            ).joinToString(TrackingConstant.SEPARATOR),
            label = "$state - $value"
        )

        eventMap[TrackingConstant.BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        eventMap[TrackingConstant.CURRENT_SITE] = TrackingConstant.TOKOPEDIASELLER

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendSectionTooltipClickEvent(pageSource: String, sectionTitle: String) {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            category = getEventCategory(pageSource),
            action = TrackingConstant.CLICK_SECTION_TOOLTIP,
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

    fun sendCalendarImpressionEvent(pageSource: String, dateFilter: DateFilterItem?) {
        val label = if (dateFilter == null) {
            String.EMPTY
        } else {
            val startDate = dateFilter.startDate
            val endDate = dateFilter.endDate
            val startDateFmt = if (startDate == null) {
                String.EMPTY
            } else {
                DateTimeUtil.format(startDate.time, DateTimeUtil.FORMAT_DD_MMM_YYYY)
            }
            val endDateFmt = if (endDate == null) {
                String.EMPTY
            } else {
                DateTimeUtil.format(endDate.time, DateTimeUtil.FORMAT_DD_MMM_YYYY)
            }
            String.format(
                TrackingConstant.DATE_FILTER_FORMAT,
                dateFilter.getDateFilterType(),
                startDateFmt,
                endDateFmt
            )
        }

        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            action = TrackingConstant.IMPRESSION_CALENDAR_ACTION_MENU,
            category = getEventCategory(pageSource),
            label = label
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

    fun sendThreeDotsImpressionEvent(pageSource: String) {
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            action = TrackingConstant.IMPRESSION_3_DOT,
            category = getEventCategory(pageSource),
            label = String.EMPTY
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendThreeDotsClickEvent(pageSource: String) {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            action = TrackingConstant.CLICK_MENU_3_DOT,
            label = String.EMPTY,
            category = getEventCategory(pageSource)
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

    fun sendImpressionExclusiveIdentifierDateFilter(pageSource: String) {
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            action = TrackingConstant.IMPRESSION_PAYWALL_DATE_FILTER,
            category = getEventCategory(pageSource),
            label = String.EMPTY
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendImpressionExclusiveFeatureDateFilter(userId: String) {
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_STATISTIC_IRIS,
            action = TrackingConstant.IMPRESSION_EXCLUSIVE_FEATURE,
            category = TrackingConstant.STATISTIC_FIREWALL,
            label = String.EMPTY,
            userId = userId
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendClickEventOnCtaExclusiveIdentifier(pageSource: String) {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            action = TrackingConstant.CLICK_PAYWALL_CTA,
            category = getEventCategory(pageSource),
            label = String.EMPTY
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendClickEventOnCloseDateFilter(pageSource: String) {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            action = TrackingConstant.CLICK_DISMISS_DATE_FILTER,
            category = getEventCategory(pageSource),
            label = String.EMPTY
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMultiLineGraphImpressionEvent(
        page: StatisticPageUiModel,
        element: MultiLineGraphWidgetUiModel
    ) {
        val isEmpty = element.data?.metrics?.all { it.isEmpty }.orFalse()
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            action = TrackingConstant.IMPRESSION_WIDGET_MULTI_TREND_LINE,
            category = page.pageSource.replace("-", " "),
            label = if (isEmpty) {
                TrackingConstant.EMPTY
            } else {
                TrackingConstant.NOT_EMPTY
            }
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTickerImpressionEvent(page: StatisticPageUiModel) {
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            action = TrackingConstant.IMPRESSION_LEARN_MORE,
            category = page.pageSource.replace("-", " "),
            label = String.EMPTY
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTickerCtaClickEvent(page: StatisticPageUiModel) {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            action = TrackingConstant.CLICK_TICKER_CTA,
            category = page.pageSource.replace("-", " "),
            label = String.EMPTY
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTrafficInsightImpressionCoachMarkEvent(pageSource: String, title: String) {
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            action = TrackingConstant.IMPRESSION_TRAFFIC_COACH_MARK,
            category = pageSource.replace("-", " "),
            label = title
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTrafficInsightCoachMarkCtaClickEvent(pageSource: String, title: String) {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            action = TrackingConstant.CLICK_TRAFFIC_COACH_MARK_CTA,
            category = pageSource.replace("-", " "),
            label = title
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendAnnouncementImpressionEvent(page: StatisticPageUiModel) {
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            action = TrackingConstant.IMPRESSION_TOP_ADS_CROSS_SELLING,
            category = page.pageSource.replace("-", " "),
            label = String.EMPTY
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendAnnouncementCtaClickEvent(page: StatisticPageUiModel) {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            action = TrackingConstant.CLICK_TOP_ADS_CROSS_SELLING,
            category = page.pageSource.replace("-", " "),
            label = String.EMPTY
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTooltipImpressionEvent(page: StatisticPageUiModel) {
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            action = TrackingConstant.IMPRESSION_EXPLANATION_TOOLTIP,
            category = page.pageSource.replace("-", " "),
            label = String.EMPTY
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendShowTableTableFilterClickEvent(
        page: StatisticPageUiModel,
        element: TableWidgetUiModel
    ) {
        val isEmpty = element.data?.dataSet?.all { it.rows.isEmpty() }.orFalse()
        val label = arrayOf(
            element.data,
            if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY,
            element.title
        ).joinToString(TrackingConstant.SEPARATOR)
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            action = TrackingConstant.CLICK_TABLE_WIDGET_SORT,
            category = page.pageSource.replace("-", " "),
            label = label
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    private fun createEventMap(
        event: String,
        action: String,
        label: String,
        category: String = TrackingConstant.SELLER_APP_STATISTIC,
        userId: String = String.EMPTY
    ): MutableMap<String, Any> {
        return TrackingHelper.createMap(
            event = event,
            category = category,
            action = action,
            label = label
        ).apply {
            if (userId.isNotBlank()) {
                this[TrackingConstant.USER_ID] = userId
            }
        }
    }

    private fun getEventCategory(pageSource: String): String {
        return when (pageSource) {
            Const.PageSource.SHOP_INSIGHT -> TrackingConstant.CATEGORY_SHOP_INSIGHT
            Const.PageSource.PRODUCT_INSIGHT -> TrackingConstant.CATEGORY_PRODUCT_INSIGHT
            Const.PageSource.TRAFFIC_INSIGHT -> TrackingConstant.CATEGORY_TRAFFIC_INSIGHT
            Const.PageSource.OPERATIONAL_INSIGHT -> TrackingConstant.CATEGORY_OPERATIONAL_INSIGHT
            Const.PageSource.BUYER_INSIGHT -> TrackingConstant.CATEGORY_BUYER_INSIGHT
            else -> String.EMPTY
        }
    }
}