package com.tokopedia.sellerhomecommon.common.const

import com.tokopedia.sellerhomecommon.common.WidgetType

/**
 * Constants that represent expected height of each widget loading state view
 */
object WidgetHeight {

    private const val CARD = 60f
    private const val CAROUSEL = 300f
    private const val DESCRIPTION = 212f
    private const val LINE_GRAPH = 320f
    private const val POST_LIST = 320f
    private const val PROGRESS = 212f
    private const val TABLE = 369f
    private const val PIE_CHART = 320f
    private const val BAR_CHART = 320f
    private const val MULTI_LINE_GRAPH = 352f
    private const val ANNOUNCEMENT = 80f
    private const val RECOMMENDATION = 252f
    private const val SECTION = 42f
    private const val MILESTONE = 320f
    private const val CALENDAR = 320f
    private const val UNIFICATION = 320f
    private const val RICH_LIST = 300f

    fun getWidgetHeight(widgetType: String): Float {
        return when (widgetType) {
            WidgetType.CARD -> CARD
            WidgetType.CAROUSEL -> CAROUSEL
            WidgetType.DESCRIPTION -> DESCRIPTION
            WidgetType.LINE_GRAPH -> LINE_GRAPH
            WidgetType.POST_LIST -> POST_LIST
            WidgetType.PROGRESS -> PROGRESS
            WidgetType.SECTION -> SECTION
            WidgetType.TABLE -> TABLE
            WidgetType.PIE_CHART -> PIE_CHART
            WidgetType.BAR_CHART -> BAR_CHART
            WidgetType.MULTI_LINE_GRAPH -> MULTI_LINE_GRAPH
            WidgetType.ANNOUNCEMENT -> ANNOUNCEMENT
            WidgetType.RECOMMENDATION -> RECOMMENDATION
            WidgetType.MILESTONE -> MILESTONE
            WidgetType.CALENDAR -> CALENDAR
            WidgetType.UNIFICATION -> UNIFICATION
            WidgetType.RICH_LIST -> RICH_LIST
            else -> 0f
        }
    }

}