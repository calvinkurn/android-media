package com.tokopedia.charts.config.linechart

import android.graphics.Typeface
import com.tokopedia.charts.common.ChartTooltip
import com.tokopedia.charts.config.linechart.annotation.LineChartDsl
import com.tokopedia.charts.config.linechart.model.*

/**
 * Created By @ilhamsuaib on 25/06/20
 */

@LineChartDsl
class LineChartConfigBuilder {

    companion object {
        fun getDefaultConfig(): LineChartConfig {
            return LineChartConfigBuilder().build()
        }

        fun create(lambda: LineChartConfigBuilder.() -> Unit) = LineChartConfigBuilder().apply(lambda).build()
    }

    private var isShowTooltipEnabled: Boolean = true
    private var isScaleXEnabled: Boolean = false
    private var isPitchZoomEnabled: Boolean = false
    private var isDescriptionEnabled: Boolean = false
    private var xAnimationDuration: Int = 0
    private var yAnimationDuration: Int = 0

    private var xAxis: XAxisConfig = XAxisConfig()
    private var leftAxisConfig: LeftAxisConfig = LeftAxisConfig()
    private var rightAxisConfig: RightAxisConfig = RightAxisConfig()
    private var legendConfig: LegendConfig = LegendConfig()
    private var tooltip: ChartTooltip? = null

    fun showTooltipEnabled(lambda: () -> Boolean) {
        isShowTooltipEnabled = lambda()
    }

    fun scaleEnabled(lambda: () -> Boolean) {
        isScaleXEnabled = lambda()
    }

    fun pitchZoomEnabled(lambda: () -> Boolean) {
        isPitchZoomEnabled = lambda()
    }

    fun descriptionEnabled(lambda: () -> Boolean) {
        isDescriptionEnabled = lambda()
    }

    fun xAnimationDuration(lambda: () -> Int) {
        xAnimationDuration = lambda()
    }

    fun yAnimationDuration(lambda: () -> Int) {
        yAnimationDuration = lambda()
    }

    fun setXAxis(xAxis: XAxisConfig) {
        this.xAxis = xAxis
    }

    fun xAxis(lambda: XAxisBuilder.() -> Unit) {
        xAxis = XAxisBuilder().apply(lambda).build()
    }

    fun leftAxis(lambda: LeftAxisBuilder.() -> Unit) {
        leftAxisConfig = LeftAxisBuilder().apply(lambda).build()
    }

    fun rightAxis(lambda: RightAxisBuilder.() -> Unit) {
        rightAxisConfig = RightAxisBuilder().apply(lambda).build()
    }

    fun legend(lambda: LegendBuilder.() -> Unit) {
        legendConfig = LegendBuilder().apply(lambda).build()
    }

    fun setTooltip(tooltip: ChartTooltip) {
        this.tooltip = tooltip
    }

    fun build(): LineChartConfig {
        return LineChartConfig(
                isDrawMarkersEnabled = isShowTooltipEnabled,
                isScaleXEnabled = isScaleXEnabled,
                isPitchZoomEnabled = isPitchZoomEnabled,
                isDescriptionEnabled = isDescriptionEnabled,
                xAnimationDuration = xAnimationDuration,
                yAnimationDuration = yAnimationDuration,
                xAxisConfig = xAxis,
                leftAxisConfig = leftAxisConfig,
                rightAxisConfig = rightAxisConfig,
                legendConfig = legendConfig,
                tooltip = tooltip
        )
    }
}

@LineChartDsl
class XAxisBuilder {
    private var typeface: Typeface? = null
    private var position: Int = XAxisConfig.BOTTOM

    fun typeface(lambda: () -> Typeface) {
        typeface = lambda()
    }

    fun position(lambda: () -> Int) {
        position = lambda()
    }

    fun build(): XAxisConfig {
        return XAxisConfig(typeface, position)
    }
}

@LineChartDsl
class LeftAxisBuilder {
    private var typeface: Typeface? = null
    private var position: Int = LeftAxisConfig.OUTSIDE_CHART

    fun typeface(lambda: () -> Typeface) {
        typeface = lambda()
    }

    fun position(lambda: () -> Int) {
        position = lambda()
    }

    fun build(): LeftAxisConfig {
        return LeftAxisConfig(typeface, position)
    }
}

@LineChartDsl
class RightAxisBuilder {
    private var isEnabled: Boolean = false

    fun enabled(lambda: () -> Boolean) {
        isEnabled = lambda()
    }

    fun build() = RightAxisConfig(isEnabled)
}

@LineChartDsl
class LegendBuilder {
    private var isEnabled: Boolean = false

    fun enabled(lambda: () -> Boolean) {
        isEnabled = lambda()
    }

    fun build() = LegendConfig(isEnabled)
}