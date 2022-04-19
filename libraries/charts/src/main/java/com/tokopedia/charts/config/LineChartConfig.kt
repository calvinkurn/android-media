package com.tokopedia.charts.config

import android.graphics.Color
import com.tokopedia.charts.common.ChartColor
import com.tokopedia.charts.config.annotation.ChartConfigDsl
import com.tokopedia.charts.model.LineChartConfigModel
import com.tokopedia.charts.view.LineChartView

/**
 * Created By @ilhamsuaib on 15/07/20
 */

@ChartConfigDsl
class LineChartConfig : BaseChartConfig() {

    companion object {
        fun getDefaultConfig(): LineChartConfigModel = create { }

        fun create(lambda: LineChartConfig.() -> Unit) = LineChartConfig().apply(lambda).build()
    }

    private var chartLineMode: Int = LineChartView.LINE_MODE_LINEAR
    private var chartDotColor: Int = Color.parseColor(ChartColor.DMS_DEFAULT_LINE_CHART_DOT_COLOR)
    private var isChartDotEnabled: Boolean = false
    private var isChartDotHoleEnabled: Boolean = false

    /**
     * chartLineMode can be set to LineChartView.LINE_MODE_LINEAR or LineChartView.LINE_MODE_CURVE
     * */
    fun chartLineMode(lambda: () -> Int) {
        chartLineMode = lambda()
    }

    fun chartDotColor(lambda: () -> Int) {
        chartDotColor = lambda()
    }

    fun chartDotEnabled(lambda: () -> Boolean) {
        isChartDotEnabled = lambda()
    }

    fun chartDotHoleEnabled(lambda: () -> Boolean) {
        isChartDotHoleEnabled = lambda()
    }

    internal fun build(): LineChartConfigModel {
        return LineChartConfigModel(
                isTooltipEnabled = isTooltipEnabled,
                isScaleXEnabled = isScaleXEnabled,
                isPitchZoomEnabled = isPitchZoomEnabled,
                isShowValueEnabled = isShowValueEnabled,
                xAnimationDuration = xAnimationDuration,
                yAnimationDuration = yAnimationDuration,
                xAxisConfig = xAxis,
                yAxisConfig = yAxis,
                tooltip = tooltip,
                chartLineMode = chartLineMode,
                chartDotColor = chartDotColor,
                isChartDotEnabled = isChartDotEnabled,
                isChartDotHoleEnabled = isChartDotHoleEnabled
        )
    }
}