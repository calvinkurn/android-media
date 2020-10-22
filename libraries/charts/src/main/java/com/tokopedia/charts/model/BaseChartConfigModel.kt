package com.tokopedia.charts.model

import com.tokopedia.charts.common.ChartTooltip

/**
 * Created By @ilhamsuaib on 15/07/20
 */

interface BaseChartConfigModel {
    val isTooltipEnabled: Boolean
    val isScaleXEnabled: Boolean
    val isPitchZoomEnabled: Boolean
    val isShowValueEnabled: Boolean
    val xAnimationDuration: Int
    val yAnimationDuration: Int
    val xAxisConfig: XAxisConfigModel
    val yAxisConfig: YAxisConfigModel
    val tooltip: ChartTooltip?
}