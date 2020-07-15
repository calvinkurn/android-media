package com.tokopedia.charts.config

import com.tokopedia.charts.config.annotation.ChartConfigDsl
import com.tokopedia.charts.model.LineChartConfigModel

/**
 * Created By @ilhamsuaib on 15/07/20
 */

@ChartConfigDsl
class LineChartConfig : BaseChartConfig() {

    companion object {
        fun getDefaultConfig(): LineChartConfigModel = create { }

        fun create(lambda: LineChartConfig.() -> Unit) = LineChartConfig().apply(lambda).build()
    }

    fun build(): LineChartConfigModel {
        return LineChartConfigModel(
                isTooltipEnabled = isTooltipEnabled,
                isScaleXEnabled = isScaleXEnabled,
                isPitchZoomEnabled = isPitchZoomEnabled,
                isShowValueEnabled = isShowValueEnabled,
                xAnimationDuration = xAnimationDuration,
                yAnimationDuration = yAnimationDuration,
                xAxisConfig = xAxis,
                yAxisConfig = yAxis,
                tooltip = tooltip
        )
    }
}