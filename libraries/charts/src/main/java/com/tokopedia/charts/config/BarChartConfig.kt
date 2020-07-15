package com.tokopedia.charts.config

import com.tokopedia.charts.config.annotation.ChartConfigDsl
import com.tokopedia.charts.model.BarChartConfigModel

/**
 * Created By @ilhamsuaib on 15/07/20
 */

@ChartConfigDsl
class BarChartConfig : BaseChartConfig() {

    companion object {
        fun getDefaultConfig(): BarChartConfigModel = create { }

        fun create(lambda: BarChartConfig.() -> Unit) = BarChartConfig().apply(lambda).build()
    }

    var isRoundedBarEnabled: Boolean = true
        private set
    var barBorderRadius: Int = 8
        private set
    var highLightAlpha: Int = 25
        private set

    fun roundedBarEnabled(lambda: () -> Boolean) {
        isRoundedBarEnabled = lambda()
    }

    fun barBorderRadius(lambda: () -> Int) {
        barBorderRadius = lambda()
    }

    fun barHighLightAlpha(lambda: () -> Int) {
        highLightAlpha = lambda()
    }

    fun build(): BarChartConfigModel {
        return BarChartConfigModel(
                isTooltipEnabled = isTooltipEnabled,
                isScaleXEnabled = isScaleXEnabled,
                isPitchZoomEnabled = isPitchZoomEnabled,
                isShowValueEnabled = isShowValueEnabled,
                xAnimationDuration = xAnimationDuration,
                yAnimationDuration = yAnimationDuration,
                xAxisConfig = xAxis,
                yAxisConfig = yAxis,
                tooltip = tooltip,
                isRoundedBarEnabled = isRoundedBarEnabled,
                barBorderRadius = barBorderRadius,
                highLightAlpha = highLightAlpha
        )
    }
}