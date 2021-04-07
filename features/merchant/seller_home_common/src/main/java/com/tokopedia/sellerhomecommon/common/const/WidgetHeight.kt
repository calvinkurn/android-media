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
    private const val POST_LIST = 352f
    private const val PROGRESS = 212f
    private const val TABLE = 369f
    private const val PIE_CHART = 320f
    private const val BAR_CHART = 320f
    private const val MULTI_LINE_GRAPH = 352f
    private const val ANNOUNCEMENT = 80f
    private const val SECTION = 42f

    fun getWidgetHeight(widgetType: String): Float {
        return when(widgetType) {
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
            else -> 0f
        }
    }

}