package com.tokopedia.charts.config

import android.graphics.Color
import android.graphics.drawable.Drawable
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

    private var chartLineWidth: Float = 1f
    private var chartLineColor: Int = Color.parseColor(ChartColor.DEFAULT_LINE_COLOR)
    private var chartLineMode: Int = LineChartView.LINE_MODE_LINEAR
    private var chartFillColor: Int = Color.parseColor(ChartColor.DEFAULT_LINE_CHART_FILL_COLOR)
    private var chartDotColor: Int = Color.parseColor(ChartColor.DEFAULT_LINE_CHART_DOT_COLOR)
    private var fillDrawable: Drawable? = null
    private var isChartFillEnabled: Boolean = true
    private var isChartDotEnabled: Boolean = false
    private var isChartDotHoleEnabled: Boolean = false

    fun chartLineWidth(lambda: () -> Float) {
        chartLineWidth = lambda()
    }

    fun chartLineColor(lambda: () -> Int) {
        chartLineColor = lambda()
    }

    fun chartFillColor(lambda: () -> Int) {
        chartFillColor = lambda()
    }

    /**
     * chartLineMode can be set to LineChartView.LINE_MODE_LINEAR or LineChartView.LINE_MODE_CURVE
     * */
    fun chartLineMode(lambda: () -> Int) {
        chartLineMode = lambda()
    }

    fun chartDotColor(lambda: () -> Int) {
        chartDotColor = lambda()
    }

    fun fillDrawable(lambda: () -> Drawable) {
        fillDrawable = lambda()
    }

    fun chartFillEnabled(lambda: () -> Boolean) {
        isChartFillEnabled = lambda()
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
                chartLineWidth = chartLineWidth,
                chartLineColor = chartLineColor,
                chartLineMode = chartLineMode,
                chartFillColor = chartFillColor,
                chartDotColor = chartDotColor,
                fillDrawable = fillDrawable,
                isChartFillEnabled = isChartFillEnabled,
                isChartDotEnabled = isChartDotEnabled,
                isChartDotHoleEnabled = isChartDotHoleEnabled
        )
    }
}