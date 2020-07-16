package com.tokopedia.charts.common

/**
 * Created By @ilhamsuaib on 14/07/20
 */

object ChartColor {

    const val DEFAULT_LINE_COLOR = "#4FBA68"
    const val DEFAULT_LINE_CHART_FILL_COLOR = "#35d6ffde"
    const val DEFAULT_LINE_CHART_DOT_COLOR = "#4FBA68"
    const val DEFAULT_BAR_COLOR = "#4FBA68"
    private val barColors = listOf(DEFAULT_BAR_COLOR, "#00B2C6", "#E347B4", "#FA9D59", "#6B7AD8")

    fun getHexColorByIndex(index: Int): String {
        return try {
            barColors[index]
        } catch (e: IndexOutOfBoundsException) {
            DEFAULT_BAR_COLOR
        }
    }
}