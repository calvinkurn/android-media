package com.tokopedia.charts.model

import com.tokopedia.charts.common.ChartTooltip

/**
 * Created By @ilhamsuaib on 15/07/20
 */

data class BarChartConfigModel(
    override val isTooltipEnabled: Boolean,
    override val isScaleXEnabled: Boolean,
    override val isPitchZoomEnabled: Boolean,
    override val isShowValueEnabled: Boolean,
    override val xAnimationDuration: Int,
    override val yAnimationDuration: Int,
    override val xAxisConfig: XAxisConfigModel,
    override val yAxisConfig: YAxisConfigModel,
    override val tooltip: ChartTooltip?,
    val isRoundedBarEnabled: Boolean,
    val barBorderRadius: Int,
    val highLightAlpha: Int,
    val isStackedBar: Boolean
) : BaseChartConfigModel
