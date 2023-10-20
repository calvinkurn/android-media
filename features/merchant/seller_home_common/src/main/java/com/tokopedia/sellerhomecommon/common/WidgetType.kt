package com.tokopedia.sellerhomecommon.common

import com.tokopedia.kotlin.extensions.view.asLowerCase

/**
 * Created By @ilhamsuaib on 21/05/20
 */

object WidgetType {

    const val CARD = "card"
    const val LINE_GRAPH = "lineGraph"
    const val CAROUSEL = "carouselImage"
    const val DESCRIPTION = "description"
    const val SECTION = "section"
    const val PROGRESS = "progressBar"
    const val POST_LIST = "post"
    const val TABLE = "searchTable"
    const val PIE_CHART = "pieChart"
    const val BAR_CHART = "barChart"
    const val MULTI_LINE_GRAPH = "multiTrendline"
    const val ANNOUNCEMENT = "announcement"
    const val RECOMMENDATION = "recommendation"
    const val MILESTONE = "milestone"
    const val CALENDAR = "calendar"
    const val UNIFICATION = "navigationTab"
    const val RICH_LIST = "richList"
    const val MULTI_COMPONENT = "multiComponent"

    fun isValidWidget(widgetType: String): Boolean {
        return getWidgetList().any { it.asLowerCase() == widgetType.asLowerCase() }
    }

    private fun getWidgetList(): List<String> {
        return listOf(
            CARD,
            CAROUSEL,
            DESCRIPTION,
            LINE_GRAPH,
            POST_LIST,
            PROGRESS,
            SECTION,
            TABLE,
            PIE_CHART,
            BAR_CHART,
            MULTI_LINE_GRAPH,
            ANNOUNCEMENT,
            RECOMMENDATION,
            MILESTONE,
            CALENDAR,
            UNIFICATION,
            RICH_LIST,
            MULTI_COMPONENT
        )
    }
}
