package com.tokopedia.sellerhome.common

/**
 * Created By @ilhamsuaib on 2020-01-21
 */
object WidgetType {

    const val CARD = "card"
    const val LINE_GRAPH = "lineGraph"
    const val CAROUSEL = "carouselImage"
    const val DESCRIPTION = "description"
    const val SECTION = "section"
    const val PROGRESS = "progressBar"
    const val POST_LIST = "post"

    fun isValidWidget(widgetType: String): Boolean {
        return getWidgetList().contains(widgetType)
    }

    fun getWidgetList(): List<String> {
        return listOf(
                CARD,
                CAROUSEL,
                DESCRIPTION,
                LINE_GRAPH,
                POST_LIST,
                PROGRESS,
                SECTION
        )
    }
}