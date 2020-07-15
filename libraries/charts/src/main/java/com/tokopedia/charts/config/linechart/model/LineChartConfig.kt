package com.tokopedia.charts.config.linechart.model

import com.tokopedia.charts.common.ChartTooltip

/**
 * Created By @ilhamsuaib on 25/06/20
 */

data class LineChartConfig(
        val isDrawMarkersEnabled: Boolean = true,
        val isScaleXEnabled: Boolean = false,
        val isPitchZoomEnabled: Boolean = false,
        val isDescriptionEnabled: Boolean = false,
        val xAnimationDuration: Int = 0,
        val yAnimationDuration: Int = 0,
        val xAxisConfig: XAxisConfig = XAxisConfig(),
        val leftAxisConfig: LeftAxisConfig = LeftAxisConfig(),
        val rightAxisConfig: RightAxisConfig = RightAxisConfig(),
        val legendConfig: LegendConfig = LegendConfig(),
        val tooltip: ChartTooltip? = null
)