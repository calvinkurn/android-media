package com.tokopedia.sellerhome.analytic

import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseMilestoneMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarEventUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineMetricUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationTabUiModel
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.unifycomponents.ticker.Ticker

/**
 * Created By @ilhamsuaib on 2020-02-11
 */

/**
 * Seller Home Tracker
 * Data Tracker :
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/1733
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/1732
 * https://mynakama.tokopedia.com/datatracker/requestdetail/781
 * */

object SellerHomeTracking {

    fun sendImpressionCardEvent(
        dataKey: String,
        state: String,
        cardValue: String,
        isSingle: Boolean
    ) {
        val subtitle = if (isSingle) {
            TrackingConstant.SINGLE
        } else {
            TrackingConstant.MULTIPLE
        }
        val map = createEventMap(
            event = TrackingConstant.VIEW_HOMEPAGE_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_WIDGET_CARD,
            label = arrayOf(dataKey, state, cardValue, subtitle).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickCardEvent(dataKey: String, state: String, cardValue: String, isSingle: Boolean) {
        val subtitle = if (isSingle) {
            TrackingConstant.SINGLE
        } else {
            TrackingConstant.MULTIPLE
        }
        val map = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.CLICK_WIDGET_CARD,
            label = arrayOf(dataKey, state, cardValue, subtitle).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionLineGraphEvent(model: LineGraphWidgetUiModel) {
        val emptyStatus =
            if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val dataKey = model.dataKey
        val title = model.data?.header.orEmpty()

        val map = createEventMap(
            TrackingConstant.VIEW_HOMEPAGE_IRIS,
            TrackingConstant.SELLER_APP_HOME,
            TrackingConstant.IMPRESSION_WIDGET_LINE_GRAPH,
            arrayOf(dataKey, emptyStatus, title).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickLineGraphEvent(model: LineGraphWidgetUiModel) {
        val emptyStatus =
            if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val dataKey = model.dataKey
        val title = model.data?.header.orEmpty()

        val map = createEventMap(
            TrackingConstant.CLICK_HOMEPAGE,
            TrackingConstant.SELLER_APP_HOME,
            arrayOf(
                TrackingConstant.CLICK_WIDGET_LINE_GRAPH,
                TrackingConstant.SEE_MORE
            ).joinToString(" - "),
            arrayOf(dataKey, emptyStatus, title).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickEmptyCtaLineGraphEvent(model: LineGraphWidgetUiModel) {
        val emptyStatus =
            if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val dataKey = model.dataKey
        val title = model.data?.header.orEmpty()

        val map = createEventMap(
            TrackingConstant.CLICK_HOMEPAGE,
            TrackingConstant.SELLER_APP_HOME,
            arrayOf(
                TrackingConstant.CLICK_WIDGET_LINE_GRAPH,
                TrackingConstant.EMPTY_STATE
            ).joinToString(" - "),
            arrayOf(dataKey, emptyStatus, title).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionDescriptionEvent(dataKey: String) {
        val map = createEventMap(
            TrackingConstant.VIEW_HOMEPAGE_IRIS,
            TrackingConstant.SELLER_APP_HOME,
            TrackingConstant.IMPRESSION_WIDGET_DESCRIPTION,
            dataKey
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickDescriptionEvent(dataKey: String) {
        val map = createEventMap(
            TrackingConstant.CLICK_HOMEPAGE,
            TrackingConstant.SELLER_APP_HOME,
            arrayOf(
                TrackingConstant.CLICK_WIDGET_DESCRIPTION,
                TrackingConstant.SEE_MORE
            ).joinToString(" - "),
            dataKey
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionProgressBarEvent(dataKey: String, stateColor: String, valueScore: Long) {
        val map = createEventMap(
            event = TrackingConstant.VIEW_HOMEPAGE_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_WIDGET_PROGRESS_BAR,
            label = arrayOf(dataKey, stateColor, valueScore).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickProgressBarEvent(dataKey: String, stateColor: String, valueScore: Long) {
        val map = createEventMap(
            TrackingConstant.CLICK_HOMEPAGE,
            TrackingConstant.SELLER_APP_HOME,
            arrayOf(
                TrackingConstant.CLICK_WIDGET_PROGRESS_BAR,
                TrackingConstant.SEE_MORE
            ).joinToString(" - "),
            arrayOf(dataKey, stateColor, valueScore).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendImpressionPostEvent(model: PostListWidgetUiModel) {
        val emptyStatus =
            if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val filterName: String = model.postFilter.find { it.isSelected }?.value.orEmpty()

        val map = createEventMap(
            TrackingConstant.VIEW_HOMEPAGE_IRIS,
            TrackingConstant.SELLER_APP_HOME,
            TrackingConstant.IMPRESSION_WIDGET_POST,
            arrayOf(model.dataKey, emptyStatus, filterName).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickPostSeeMoreEvent(model: PostListWidgetUiModel) {
        val emptyStatus =
            if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val filterName: String = model.postFilter.find { it.isSelected }?.value.orEmpty()

        val map = createEventMap(
            TrackingConstant.CLICK_HOMEPAGE,
            TrackingConstant.SELLER_APP_HOME,
            arrayOf(
                TrackingConstant.CLICK_WIDGET_POST,
                TrackingConstant.SEE_MORE
            ).joinToString(" - "),
            arrayOf(model.dataKey, emptyStatus, filterName).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickPostItemEvent(model: PostListWidgetUiModel, post: PostItemUiModel) {
        val emptyStatus: String = if (model.isEmpty()) {
            TrackingConstant.EMPTY
        } else {
            TrackingConstant.NOT_EMPTY
        }
        val filterName: String = model.postFilter.find { it.isSelected }?.value.orEmpty()

        val stateMediaUrl: String
        val stateText: String
        if (post is PostItemUiModel.PostTextEmphasizedUiModel) {
            stateMediaUrl = post.stateMediaUrl
            stateText = post.stateText
        } else {
            stateMediaUrl = ""
            stateText = ""
        }

        val map = createEventMap(
            TrackingConstant.CLICK_HOMEPAGE,
            TrackingConstant.SELLER_APP_HOME,
            arrayOf(TrackingConstant.CLICK_WIDGET_POST, TrackingConstant.POST).joinToString(" - "),
            arrayOf(
                model.dataKey, emptyStatus, filterName,
                post.title, stateMediaUrl, stateText
            ).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendPostListFilterClick(model: PostListWidgetUiModel) {
        val emptyStatus =
            if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val filterName: String = model.postFilter.find { it.isSelected }?.value.orEmpty()

        val map = createEventMap(
            TrackingConstant.CLICK_HOMEPAGE,
            TrackingConstant.SELLER_APP_HOME,
            arrayOf(
                TrackingConstant.CLICK_WIDGET_POST,
                TrackingConstant.FILTER
            ).joinToString(" - "),
            arrayOf(model.dataKey, emptyStatus, filterName).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendPostEmptyStateCtaClick(model: PostListWidgetUiModel) {
        val emptyStatus =
            if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val filterName: String = model.postFilter.find { it.isSelected }?.value.orEmpty()

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_POST,
                TrackingConstant.EMPTY_STATE
            ).joinToString(" - "),
            label = arrayOf(model.dataKey, emptyStatus, filterName).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendClickCarouselCtaEvent(dataKey: String) {
        val map = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_BANNER,
                TrackingConstant.SEE_MORE
            ).joinToString(" - "),
            label = dataKey
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendClickCarouselItemBannerEvent(
        dataKey: String,
        items: List<CarouselItemUiModel>,
        position: Int
    ) {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.CLICK_WIDGET_BANNER,
            label = arrayOf(dataKey, items[position].id, position.toString()).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendImpressionCarouselItemBannerEvent(
        dataKey: String,
        items: List<CarouselItemUiModel>,
        position: Int
    ) {
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_HOMEPAGE_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_WIDGET_BANNER,
            label = arrayOf(dataKey, items[position].id, position.toString()).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTableImpressionEvent(model: TableWidgetUiModel, isSlideEmpty: Boolean) {
        val state = if (isSlideEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_HOMEPAGE_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_WIDGET_TABLE,
            label = arrayOf(model.dataKey, state).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTableOnSwipeEvent(
        model: TableWidgetUiModel,
        slidePosition: Int,
        slideCount: Int,
        isSlideEmpty: Boolean
    ) {
        val state = if (isSlideEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_ADVANCE_TABLE,
                TrackingConstant.SWIPE
            ).joinToString(" - "),
            label = arrayOf(model.dataKey, state, slidePosition, slideCount).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTableSeeMoreClickEvent(model: TableWidgetUiModel, isSlideEmpty: Boolean) {
        val state = if (isSlideEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_ADVANCE_TABLE,
                TrackingConstant.SEE_MORE
            ).joinToString(" - "),
            label = arrayOf(model.dataKey, state).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTableClickHyperlinkEvent(
        dataKey: String,
        url: String,
        isEmpty: Boolean
    ) {
        val state = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val map = createEventMap(
            TrackingConstant.CLICK_HOMEPAGE,
            TrackingConstant.SELLER_APP_HOME,
            arrayOf(TrackingConstant.CLICK_WIDGET_ADVANCE_TABLE, dataKey).joinToString(" - "),
            arrayOf(state, url).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendTableEmptyStateCtaClick(model: TableWidgetUiModel) {
        val emptyStatus = if (model.isEmpty()) {
            TrackingConstant.EMPTY
        } else {
            TrackingConstant.NOT_EMPTY
        }

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_ADVANCE_TABLE,
                TrackingConstant.EMPTY_STATE
            ).joinToString(" - "),
            label = arrayOf(model.dataKey, emptyStatus).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendTableFilterClickEvent(model: TableWidgetUiModel) {
        val emptyStatus =
            if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val selectedFilter = model.tableFilters.firstOrNull { it.isSelected }?.value.orEmpty()

        val map = createEventMap(
            TrackingConstant.CLICK_HOMEPAGE,
            TrackingConstant.SELLER_APP_HOME,
            arrayOf(
                TrackingConstant.CLICK_WIDGET_ADVANCE_TABLE,
                TrackingConstant.FILTER
            ).joinToString(" - "),
            arrayOf(model.dataKey, emptyStatus, selectedFilter).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(map)
    }

    fun sendPieChartImpressionEvent(model: PieChartWidgetUiModel) {
        val state = if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_HOMEPAGE_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_WIDGET_PIE_CHART,
            label = arrayOf(model.dataKey, state, model.title).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendPieChartEmptyStateCtaClickEvent(model: PieChartWidgetUiModel) {
        val state = if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_PIE_CHART,
                TrackingConstant.EMPTY_STATE
            ).joinToString(" - "),
            label = arrayOf(model.dataKey, state).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendPieChartSeeMoreCtaClickEvent(model: PieChartWidgetUiModel) {
        val state = if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_PIE_CHART,
                TrackingConstant.SEE_MORE
            ).joinToString(" - "),
            label = arrayOf(model.dataKey, state).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendBarChartImpressionEvent(model: BarChartWidgetUiModel) {
        val state = if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val value = model.data?.chartData?.summary?.valueFmt.orEmpty()

        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_HOMEPAGE_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_WIDGET_BAR_CHART,
            label = arrayOf(model.dataKey, state, value).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendBarChartEmptyStateCtaClickEvent(model: BarChartWidgetUiModel) {
        val state = if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val value = model.data?.chartData?.summary?.valueFmt.orEmpty()

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_BAR_CHART,
                TrackingConstant.EMPTY_STATE
            ).joinToString(" - "),
            label = arrayOf(model.dataKey, state, value).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendBarChartSeeMoreClickEvent(model: BarChartWidgetUiModel) {
        val state = if (model.isEmpty()) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val value = model.data?.chartData?.summary?.valueFmt.orEmpty()

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_BAR_CHART,
                TrackingConstant.SEE_MORE
            ).joinToString(" - "),
            label = arrayOf(model.dataKey, state, value).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendAnnouncementImpressionEvent(model: AnnouncementWidgetUiModel) {
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_HOMEPAGE_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_WIDGET_ANNOUNCEMENT,
            label = model.dataKey
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendAnnouncementClickEvent(model: AnnouncementWidgetUiModel) {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.CLICK_WIDGET_ANNOUNCEMENT,
            label = model.dataKey
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMultiLineGraphMetricClick(
        model: MultiLineGraphWidgetUiModel,
        metric: MultiLineMetricUiModel
    ) {
        val emptyStatus = if (metric.isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_MULTI_LINE_GRAPH,
                TrackingConstant.TRENDLINE
            ).joinToString(" - "),
            label = arrayOf(
                model.dataKey, emptyStatus,
                metric.summary.title
            ).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMultiLineGraphCtaClick(model: MultiLineGraphWidgetUiModel) {
        val isEmpty = model.data?.metrics?.getOrNull(0)?.isEmpty ?: true
        val emptyStatus = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_MULTI_LINE_GRAPH,
                TrackingConstant.SEE_MORE
            ).joinToString(" - "),
            label = arrayOf(model.dataKey, emptyStatus).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMultiLineGraphImpressionEvent(model: MultiLineGraphWidgetUiModel) {
        val isEmpty = model.data?.metrics?.getOrNull(0)?.isEmpty ?: true
        val emptyStatus = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_HOMEPAGE_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_WIDGET_MULTI_LINE_GRAPH,
            label = arrayOf(model.dataKey, emptyStatus).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMultiLineGraphEmptyStateCtaClick(model: MultiLineGraphWidgetUiModel) {
        val isEmpty = model.data?.metrics?.getOrNull(0)?.isEmpty ?: true
        val emptyStatus = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_MULTI_LINE_GRAPH,
                TrackingConstant.EMPTY_STATE
            ).joinToString(" - "),
            label = arrayOf(model.dataKey, emptyStatus).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendRecommendationCtaClickEvent(element: RecommendationWidgetUiModel) {
        val score = element.data?.progressBar?.bar?.value.orZero()
        val level = element.data?.progressLevel?.bar?.value.orZero()
        val numOfNegativeRecommendation = getNumberOfRecommendationByType(
            element.data
                ?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_NEGATIVE
        )
        val numOfPositiveRecommendation = getNumberOfRecommendationByType(
            element.data
                ?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_POSITIVE
        )
        val numOfNoDataRecommendation = getNumberOfRecommendationByType(
            element.data
                ?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_NO_DATA
        )
        val tickerStatus = if (element.data?.ticker?.text.isNullOrBlank()) "off" else "on"
        val tickerLabel = "ticker $tickerStatus"
        val eventLabel = arrayOf(
            element.dataKey, score, level, numOfNegativeRecommendation,
            numOfPositiveRecommendation, numOfNoDataRecommendation,
            tickerLabel
        ).joinToString(" - ")

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_RECOMMENDATION,
                TrackingConstant.SEE_MORE
            ).joinToString(" - "),
            label = eventLabel
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendRecommendationTickerCtaClickEvent(element: RecommendationWidgetUiModel) {
        val score = element.data?.progressBar?.bar?.value.orZero()
        val level = element.data?.progressLevel?.bar?.value.orZero()
        val numOfNegativeRecommendation = getNumberOfRecommendationByType(
            element.data
                ?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_NEGATIVE
        )
        val numOfPositiveRecommendation = getNumberOfRecommendationByType(
            element.data
                ?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_POSITIVE
        )
        val numOfNoDataRecommendation = getNumberOfRecommendationByType(
            element.data
                ?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_NO_DATA
        )
        val tickerStatus = if (element.data?.ticker?.text.isNullOrBlank()) "off" else "on"
        val tickerLabel = "ticker $tickerStatus"
        val eventLabel = arrayOf(
            element.dataKey, score, level, numOfNegativeRecommendation,
            numOfPositiveRecommendation, numOfNoDataRecommendation,
            tickerLabel
        ).joinToString(" - ")

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_RECOMMENDATION,
                TrackingConstant.HYPERLINK
            ).joinToString(" - "),
            label = eventLabel,
            currentSite = TrackingConstant.TOKOPEDIA_MARKETPLACE
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendRecommendationImpressionEvent(element: RecommendationWidgetUiModel) {
        val score = element.data?.progressBar?.bar?.value.orZero()
        val level = element.data?.progressLevel?.bar?.value.orZero()
        val numOfNegativeRecommendation = getNumberOfRecommendationByType(
            element.data
                ?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_NEGATIVE
        )
        val numOfPositiveRecommendation = getNumberOfRecommendationByType(
            element.data
                ?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_POSITIVE
        )
        val numOfNoDataRecommendation = getNumberOfRecommendationByType(
            element.data
                ?.recommendation?.recommendations, RecommendationItemUiModel.TYPE_NO_DATA
        )
        val tickerStatus = if (element.data?.ticker?.text.isNullOrBlank()) "off" else "on"
        val tickerLabel = "ticker $tickerStatus"
        val eventLabel = arrayOf(
            element.dataKey, score, level, numOfNegativeRecommendation,
            numOfPositiveRecommendation, numOfNoDataRecommendation,
            tickerLabel
        ).joinToString(" - ")

        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_HOMEPAGE_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_WIDGET_RECOMMENDATION,
            label = eventLabel
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendRecommendationItemClickEvent(dataKey: String, item: RecommendationItemUiModel) {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = arrayOf(
                TrackingConstant.CLICK_WIDGET_RECOMMENDATION,
                TrackingConstant.RECOMMENDATION
            ).joinToString(" - "),
            label = arrayOf(dataKey, item.type, item.text).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendHomeTickerCtaClickEvent(
        tickerId: String,
        tickerType: Int
    ) {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.CLICK_TICKER_HYPERLINK,
            label = arrayOf(
                tickerId,
                getTickerTypeString(tickerType)
            ).joinToString(" - ")
        )
        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendHomeTickerImpressionEvent(
        tickerId: String,
        tickerType: Int
    ) {
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_HOMEPAGE_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_TICKER,
            label = arrayOf(
                tickerId,
                getTickerTypeString(tickerType)
            ).joinToString(" - ")
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMilestoneWidgetImpressionEvent(model: MilestoneWidgetUiModel) {
        val missions = model.data?.milestoneMissions
            ?.filterIsInstance<MilestoneMissionUiModel>().orEmpty()

        val isFinished = missions.all { it.missionCompletionStatus }

        val state = if (isFinished) {
            TrackingConstant.FINISHED
        } else {
            val totalFinishedMissions = missions.filter { it.missionCompletionStatus }.size
            String.format(
                TrackingConstant.TOTAL_FINISHED_MISSION,
                totalFinishedMissions,
                missions.size
            )
        }

        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_HOMEPAGE_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_WIDGET_MILESTONE,
            label = arrayOf(model.title, state).joinSpaceSeparator()
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMilestoneMissionImpressionEvent(
        mission: BaseMilestoneMissionUiModel,
        position: Int
    ) {
        val state = when (mission.missionButton.buttonStatus) {
            BaseMilestoneMissionUiModel.ButtonStatus.ENABLED -> TrackingConstant.ACTIVE
            BaseMilestoneMissionUiModel.ButtonStatus.DISABLED -> TrackingConstant.FINISHED
            BaseMilestoneMissionUiModel.ButtonStatus.HIDDEN -> TrackingConstant.LOCKED
        }

        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_HOMEPAGE_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_WIDGET_MILESTONE_CARD,
            label = arrayOf(mission.title, state, position).joinSpaceSeparator()
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMilestoneMissionCtaClickEvent(
        mission: BaseMilestoneMissionUiModel,
        position: Int
    ) {
        val state = when (mission.missionButton.buttonStatus) {
            BaseMilestoneMissionUiModel.ButtonStatus.ENABLED -> TrackingConstant.ACTIVE
            BaseMilestoneMissionUiModel.ButtonStatus.DISABLED -> TrackingConstant.FINISHED
            BaseMilestoneMissionUiModel.ButtonStatus.HIDDEN -> TrackingConstant.LOCKED
        }

        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.CLICK_WIDGET_MILESTONE_CARD,
            label = arrayOf(mission.title, state, position).joinSpaceSeparator()
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMilestoneFinishedMissionCtaClickEvent() {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.CLICK_WIDGET_MILESTONE_CARD_FINISHED
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMilestoneWidgetCtaClickEvent() {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.CLICK_WIDGET_MILESTONE_SEE_MORE
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMilestoneWidgetMinimizeClickEvent() {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.CLICK_WIDGET_MILESTONE_MINIMIZE
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendMilestoneMissionShareClickEvent(socialMedia: String) {
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_HOMEPAGE,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.CLICK_WIDGET_MILESTONE_SHARE,
            label = socialMedia
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendCalendarItemClickEvent(element: CalendarWidgetUiModel, event: CalendarEventUiModel) {
        val emptyLabel = TrackingConstant.NOT_EMPTY
        val dateRage = "${event.startDate} - ${event.endDate}"
        val eventTitle = "${event.label} ${event.eventName}"
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.CLICK_WIDGET_CALENDAR_EVENT,
            label = arrayOf(
                element.dataKey,
                emptyLabel,
                dateRage,
                eventTitle
            ).joinDashSeparator(),
            businessUnit = TrackingConstant.PG,
            currentSite = TrackingConstant.TOKOPEDIA_MARKETPLACE
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendCalendarFilterClickEvent(element: CalendarWidgetUiModel, dateFilter: DateFilterItem) {
        val startDate = dateFilter.startDate
        val endDate = dateFilter.endDate
        if (startDate != null && endDate != null) {
            val startDateStr = DateTimeUtil.format(startDate.time, DateTimeUtil.FORMAT_DD_MM_YYYY)
            val endDateStr = DateTimeUtil.format(endDate.time, DateTimeUtil.FORMAT_DD_MM_YYYY)
            val dateRage = "$startDateStr - $endDateStr"
            val eventMap = createEventMap(
                event = TrackingConstant.CLICK_PG,
                category = TrackingConstant.SELLER_APP_HOME,
                action = TrackingConstant.CLICK_WIDGET_CALENDAR_SELECT_DATE_RANGE,
                label = arrayOf(
                    element.dataKey,
                    TrackingConstant.NOT_EMPTY,
                    dateRage
                ).joinDashSeparator(),
                businessUnit = TrackingConstant.PG,
                currentSite = TrackingConstant.TOKOPEDIA_MARKETPLACE
            )

            TrackingHelper.sendGeneralEvent(eventMap)
        }
    }

    fun sendCalendarImpressionEvent(element: CalendarWidgetUiModel) {
        val isEmpty = element.data?.eventGroups.isNullOrEmpty()
        val emptyNotEmpty = if (isEmpty) TrackingConstant.EMPTY else TrackingConstant.NOT_EMPTY
        val dateRage =
            "${element.filter.getDateRange().startDate} - ${element.filter.getDateRange().endDate}"
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_WIDGET_CALENDAR,
            label = arrayOf(element.dataKey, emptyNotEmpty, dateRage).joinDashSeparator(),
            businessUnit = TrackingConstant.PG,
            currentSite = TrackingConstant.TOKOPEDIA_MARKETPLACE
        )

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendUnificationImpressionEvent(dataKey: String) {
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_WIDGET_UNIFICATION,
            label = dataKey
        )
        eventMap[TrackingConstant.TRACKER_ID] = "33397"

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendUnificationTabImpressionEvent(dataKey: String, tab: UnificationTabUiModel?) {
        if (tab == null) return
        val emptyLabel = if (tab.isUnauthorized) {
            TrackingConstant.NO_ACCESS
        } else {
            getEmptyLabel(tab.data?.isWidgetEmpty().orTrue())
        }
        val eventMap = createEventMap(
            event = TrackingConstant.VIEW_PG_IRIS,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.IMPRESSION_WIDGET_UNIFICATION_SEE_TAB,
            label = arrayOf(
                dataKey, tab.title, emptyLabel
            ).joinDashSeparator()
        )
        eventMap[TrackingConstant.TRACKER_ID] = "33398"

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendUnificationTabClickEvent(dataKey: String, tab: UnificationTabUiModel) {
        val emptyLabel = if (tab.isUnauthorized) {
            TrackingConstant.NO_ACCESS
        } else {
            getEmptyLabel(tab.data?.isWidgetEmpty().orTrue())
        }
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.CLICK_WIDGET_UNIFICATION_TAB,
            label = arrayOf(
                dataKey, tab.title, emptyLabel
            ).joinDashSeparator()
        )
        eventMap[TrackingConstant.TRACKER_ID] = "33399"

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendUnificationTableItemClickEvent(dataKey: String, tab: UnificationTabUiModel) {
        val emptyLabel = TrackingConstant.NOT_EMPTY
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.CLICK_WIDGET_UNIFICATION_TAB_ITEM,
            label = arrayOf(
                dataKey, tab.title, emptyLabel
            ).joinDashSeparator()
        )
        eventMap[TrackingConstant.TRACKER_ID] = "33400"

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendUnificationSeeMoreClickEvent(dataKey: String, tab: UnificationTabUiModel) {
        val emptyLabel = if (tab.isUnauthorized) {
            TrackingConstant.NO_ACCESS
        } else {
            getEmptyLabel(tab.data?.isWidgetEmpty().orTrue())
        }
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.CLICK_WIDGET_UNIFICATION_SEE_MORE,
            label = arrayOf(
                dataKey, tab.title, emptyLabel
            ).joinDashSeparator()
        )
        eventMap[TrackingConstant.TRACKER_ID] = "33401"

        TrackingHelper.sendGeneralEvent(eventMap)
    }

    fun sendUnificationEmptyStateCtaClickEvent(dataKey: String, tab: UnificationTabUiModel?) {
        if (tab == null) return
        val eventMap = createEventMap(
            event = TrackingConstant.CLICK_PG,
            category = TrackingConstant.SELLER_APP_HOME,
            action = TrackingConstant.CLICK_WIDGET_UNIFICATION_EMPTY_STATE,
            label = arrayOf(
                dataKey, tab.title, TrackingConstant.EMPTY
            ).joinDashSeparator()
        )
        eventMap[TrackingConstant.TRACKER_ID] = "33402"

        TrackingHelper.sendGeneralEvent(eventMap)
    }


    fun sendClickWidgetAnnouncementDismissalPromptEvent(dataKey: String, clickYes: Boolean) {
        val actionClick = if (clickYes) "Yes" else "No"
        val eventLabel = "$dataKey - $actionClick"
        Tracker.Builder()
            .setEvent(TrackingConstant.CLICK_PG)
            .setEventAction("click widget announcement - dismissal prompt")
            .setEventCategory(TrackingConstant.SELLER_APP_HOME)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackingConstant.TRACKER_ID, "35798")
            .setBusinessUnit(TrackingConstant.PHYSICAL_GOODS_CAPITALIZED)
            .setCurrentSite(TrackingConstant.TOKOPEDIA_MARKETPLACE)
            .build()
            .send()
    }


    fun sendClickWidgetAnnouncementSubmitDismissalEvent(dataKey: String) {
        Tracker.Builder()
            .setEvent(TrackingConstant.CLICK_PG)
            .setEventAction("click widget announcement - submit dismissal")
            .setEventCategory(TrackingConstant.SELLER_APP_HOME)
            .setEventLabel(dataKey)
            .setCustomProperty(TrackingConstant.TRACKER_ID, "35799")
            .setBusinessUnit(TrackingConstant.PHYSICAL_GOODS_CAPITALIZED)
            .setCurrentSite(TrackingConstant.TOKOPEDIA_MARKETPLACE)
            .build()
            .send()
    }

    fun sendClickWidgetAnnouncementCancelDismissalEvent(dataKey: String) {
        Tracker.Builder()
            .setEvent(TrackingConstant.CLICK_PG)
            .setEventAction("click widget announcement - cancel dismissal")
            .setEventCategory(TrackingConstant.SELLER_APP_HOME)
            .setEventLabel(dataKey)
            .setCustomProperty(TrackingConstant.TRACKER_ID, "35800")
            .setBusinessUnit(TrackingConstant.PHYSICAL_GOODS_CAPITALIZED)
            .setCurrentSite(TrackingConstant.TOKOPEDIA_MARKETPLACE)
            .build()
            .send()
    }


    fun sendClickWidgetPostDeleteEvent(dataKey: String) {
        Tracker.Builder()
            .setEvent(TrackingConstant.CLICK_PG)
            .setEventAction("click widget post - delete")
            .setEventCategory(TrackingConstant.SELLER_APP_HOME)
            .setEventLabel(dataKey)
            .setCustomProperty(TrackingConstant.TRACKER_ID, "35801")
            .setBusinessUnit(TrackingConstant.PHYSICAL_GOODS_CAPITALIZED)
            .setCurrentSite(TrackingConstant.TOKOPEDIA_MARKETPLACE)
            .build()
            .send()
    }


    fun sendClickWidgetPostSubmitDismissalEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackingConstant.CLICK_PG)
            .setEventAction("click widget post - submit dismissal")
            .setEventCategory(TrackingConstant.SELLER_APP_HOME)
            .setEventLabel(eventLabel) //"eventLabel": "{DataKey} - {Dismissed Content}"
            .setCustomProperty(TrackingConstant.TRACKER_ID, "35802")
            .setBusinessUnit(TrackingConstant.PHYSICAL_GOODS_CAPITALIZED)
            .setCurrentSite(TrackingConstant.TOKOPEDIA_MARKETPLACE)
            .build()
            .send()
    }


    fun sendClickWidgetPostCancelDismissalEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackingConstant.CLICK_PG)
            .setEventAction("click widget post - cancel dismissal")
            .setEventCategory(TrackingConstant.SELLER_APP_HOME)
            .setEventLabel(eventLabel) //"eventLabel": "{DataKey} - {Dismissed Content}",
            .setCustomProperty(TrackingConstant.TRACKER_ID, "35803")
            .setBusinessUnit(TrackingConstant.PHYSICAL_GOODS_CAPITALIZED)
            .setCurrentSite(TrackingConstant.TOKOPEDIA_MARKETPLACE)
            .build()
            .send()
    }

    fun sendScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    private fun getEmptyLabel(isEmpty: Boolean): String {
        return if (isEmpty) {
            TrackingConstant.EMPTY
        } else {
            TrackingConstant.NOT_EMPTY
        }
    }

    private fun getNumberOfRecommendationByType(
        recommendations: List<RecommendationItemUiModel>?,
        type: String
    ): Int {
        return recommendations?.filter { it.type == type }?.size.orZero()
    }

    private fun getTickerTypeString(tickerType: Int?): String {
        return when (tickerType) {
            Ticker.TYPE_ANNOUNCEMENT -> TrackingConstant.TICKER_ANNOUNCEMENT
            Ticker.TYPE_ERROR -> TrackingConstant.TICKER_DANGER
            Ticker.TYPE_INFORMATION -> TrackingConstant.TICKER_INFO
            Ticker.TYPE_WARNING -> TrackingConstant.TICKER_WARNING
            else -> ""
        }
    }

    private fun createEventMap(
        event: String,
        category: String,
        action: String,
        label: String = TrackingConstant.EMPTY_STRING,
        businessUnit: String = TrackingConstant.PHYSICAL_GOODS,
        currentSite: String = TrackingConstant.TOKOPEDIA_SELLER
    ): MutableMap<String, Any> {
        val map = TrackingHelper.createMap(event, category, action, label)
        map[TrackingConstant.BUSINESS_UNIT] = businessUnit
        map[TrackingConstant.CURRENT_SITE] = currentSite
        return map
    }

    private fun <T> Array<T>.joinDashSeparator(): String {
        return this.joinToString(TrackingConstant.SEPARATOR)
    }

    private fun <T> Array<T>.joinSpaceSeparator(): String {
        return this.joinToString(TrackingConstant.SPACE)
    }
}