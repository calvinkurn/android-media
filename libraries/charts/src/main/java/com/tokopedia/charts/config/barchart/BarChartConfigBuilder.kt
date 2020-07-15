package com.tokopedia.charts.config.barchart

import com.tokopedia.charts.common.ChartTooltip
import com.tokopedia.charts.config.barchart.model.BarChartAxisConfig
import com.tokopedia.charts.config.barchart.model.BarChartConfig

/**
 * Created By @ilhamsuaib on 09/07/20
 */

class BarChartConfigBuilder {

    companion object {
        fun getDefaultConfig(): BarChartConfig = create { }

        fun create(lambda: BarChartConfigBuilder.() -> Unit): BarChartConfig {
            return BarChartConfigBuilder().apply(lambda).build()
        }
    }

    var isRoundedBar: Boolean = true
        private set
    var drawMarkerEnabled: Boolean = false
        private set
    var showBarValueEnabled: Boolean = false
        private set
    var borderRadius: Int = 8
        private set
    var barHighLightAlpha: Int = 25
        private set
    var xAxisConfig: BarChartAxisConfig = BarChartAxisConfigBuilder.create { }
        private set
    var yAxisConfig: BarChartAxisConfig = BarChartAxisConfigBuilder.create { }
        private set
    var tooltip: ChartTooltip? = null

    fun build(): BarChartConfig {
        return BarChartConfig(
                isRoundedBar = isRoundedBar,
                drawMarkersEnabled = drawMarkerEnabled,
                showBarValueEnabled = showBarValueEnabled,
                borderRadius = borderRadius,
                highLightAlpha = barHighLightAlpha,
                tooltip = tooltip,
                xAxisConfig = xAxisConfig,
                yAxisConfig = yAxisConfig
        )
    }

    fun xAxis(lambda: BarChartAxisConfigBuilder.() -> Unit) {
        xAxisConfig = BarChartAxisConfigBuilder.create(lambda)
    }

    fun yAxis(lambda: BarChartAxisConfigBuilder.() -> Unit) {
        yAxisConfig = BarChartAxisConfigBuilder.create(lambda)
    }

    fun borderRadius(lambda: () -> Int) {
        borderRadius = lambda()
    }

    fun barHighLightAlpha(lambda: () -> Int) {
        barHighLightAlpha = lambda()
    }

    fun roundedBarEnabled(lambda: () -> Boolean) {
        isRoundedBar = lambda()
    }

    fun drawMarkerEnabled(lambda: () -> Boolean) {
        drawMarkerEnabled = lambda()
    }

    fun showBarValueEnabled(lambda: () -> Boolean) {
        showBarValueEnabled = lambda()
    }
}

class BarChartAxisConfigBuilder {

    companion object {

        fun create(lambda: BarChartAxisConfigBuilder.() -> Unit): BarChartAxisConfig {
            return BarChartAxisConfigBuilder().apply(lambda).build()
        }
    }

    var isEnabled: Boolean = true
        private set
    var showLabelEnabled: Boolean = true
        private set
    var showGridEnabled: Boolean = false
        private set
    var spaceTop: Float = 10f
        private set

    fun isEnabled(lambda: () -> Boolean) {
        isEnabled = lambda()
    }

    fun showLabelEnabled(lambda: () -> Boolean) {
        showLabelEnabled = lambda()
    }

    fun showGridEnabled(lambda: () -> Boolean) {
        showGridEnabled = lambda()
    }

    fun spaceToTop(lambda: () -> Float) {
        spaceTop = lambda()
    }

    fun build(): BarChartAxisConfig {
        return BarChartAxisConfig(
                isEnabled = isEnabled,
                showLabelEnabled = showLabelEnabled,
                showGridEnabled = showGridEnabled,
                spaceTop = spaceTop
        )
    }
}