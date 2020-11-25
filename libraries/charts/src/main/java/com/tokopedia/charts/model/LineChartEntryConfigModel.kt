package com.tokopedia.charts.model

import android.graphics.Color
import android.graphics.drawable.Drawable
import com.tokopedia.charts.common.ChartColor

/**
 * Created By @ilhamsuaib on 01/09/20
 */

data class LineChartEntryConfigModel(
        val lineWidth: Float = 1f,
        val lineColor: Int = Color.parseColor(ChartColor.DEFAULT_LINE_COLOR),
        val fillDrawable: Drawable? = null,
        val fillColor: Int = Color.parseColor(ChartColor.DEFAULT_LINE_CHART_FILL_COLOR),
        val drawFillEnabled: Boolean = true,
        val isLineDashed: Boolean = false
)