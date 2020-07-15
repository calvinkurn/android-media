package com.tokopedia.charts.common

/**
 * Created By @ilhamsuaib on 14/07/20
 */

object ChartColor {

    const val DEFAULT_COLOR = "#4FBA68"
    private val barColors = listOf(DEFAULT_COLOR, "#00B2C6", "#E347B4", "#FA9D59", "#6B7AD8")

    fun getHexColorByIndex(index: Int): String {
        return try {
            barColors[index]
        } catch (e: IndexOutOfBoundsException) {
            DEFAULT_COLOR
        }
    }
}