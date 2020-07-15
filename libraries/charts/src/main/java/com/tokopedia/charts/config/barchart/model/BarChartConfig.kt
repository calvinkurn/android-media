package com.tokopedia.charts.config.barchart.model

import com.tokopedia.charts.common.ChartTooltip

/**
 * Created By @ilhamsuaib on 09/07/20
 */

data class BarChartConfig(
        val isRoundedBar: Boolean,
        val drawMarkersEnabled: Boolean,
        val showBarValueEnabled: Boolean,
        val borderRadius: Int,
        val highLightAlpha: Int,
        val tooltip: ChartTooltip?,
        val xAxisConfig: BarChartAxisConfig,
        val yAxisConfig: BarChartAxisConfig
)