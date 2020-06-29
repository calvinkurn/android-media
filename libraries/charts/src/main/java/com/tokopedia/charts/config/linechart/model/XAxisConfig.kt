package com.tokopedia.charts.config.linechart.model

import android.graphics.Typeface
import com.github.mikephil.charting.components.XAxis
import com.tokopedia.charts.config.linechart.XAxisBuilder

/**
 * Created By @ilhamsuaib on 25/06/20
 */

data class XAxisConfig(
        val typeface: Typeface? = null,
        val position: Int = BOTTOM
) {
    companion object {
        const val BOTTOM = 0
        const val TOP = 1
        const val BOTH_SIDE = 2
        const val BOTTOM_INSIDE = 3
        const val TOP_INSIDE = 4

        fun create(lambda: XAxisBuilder.() -> Unit) = XAxisBuilder().apply(lambda).build()
    }

    fun getPosition(): XAxis.XAxisPosition {
        return when (position) {
            BOTTOM -> XAxis.XAxisPosition.BOTTOM
            TOP -> XAxis.XAxisPosition.TOP
            BOTH_SIDE -> XAxis.XAxisPosition.BOTH_SIDED
            BOTTOM_INSIDE -> XAxis.XAxisPosition.BOTTOM_INSIDE
            else -> XAxis.XAxisPosition.TOP_INSIDE
        }
    }
}