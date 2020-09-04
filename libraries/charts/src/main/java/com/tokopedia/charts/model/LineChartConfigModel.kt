package com.tokopedia.charts.model

import com.tokopedia.charts.common.ChartTooltip

/**
 * Created By @ilhamsuaib on 25/06/20
 */

data class LineChartConfigModel(
        override val isTooltipEnabled: Boolean,
        override val isScaleXEnabled: Boolean,
        override val isPitchZoomEnabled: Boolean,
        override val isShowValueEnabled: Boolean,
        override val xAnimationDuration: Int,
        override val yAnimationDuration: Int,
        override val xAxisConfig: XAxisConfigModel,
        override val yAxisConfig: YAxisConfigModel,
        override val tooltip: ChartTooltip?,
        val chartLineMode: Int,
        val chartDotColor: Int,
        val isChartDotEnabled: Boolean,
        val isChartDotHoleEnabled: Boolean
) : BaseChartConfigModel