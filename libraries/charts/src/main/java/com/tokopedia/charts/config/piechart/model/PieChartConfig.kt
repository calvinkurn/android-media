package com.tokopedia.charts.config.piechart.model

import android.graphics.Color
import com.tokopedia.charts.config.piechart.annotation.PieChartStyle

/**
 * Created By @ilhamsuaib on 07/07/20
 */

data class PieChartConfig(
        val entryLabelColor: Int = Color.BLACK,
        val entryLabelTextSize: Float = 12f,
        val xAnimationDuration: Int = 0,
        val yAnimationDuration: Int = 0,
        @PieChartStyle val chartStyle: Int = PieChartStyle.DEFAULT,
        val sliceSpaceWidth: Float = 0f,
        val isHalfChart: Boolean = false,
        val rotationEnabled: Boolean = false,
        val highlightPerTapEnabled: Boolean = false,
        val animationEnabled: Boolean = true,
        val showXValueEnabled: Boolean = false,
        val showYValueEnabled: Boolean = false,
        val touchEnabled: Boolean = false,
        val legendEnabled: Boolean = true
)