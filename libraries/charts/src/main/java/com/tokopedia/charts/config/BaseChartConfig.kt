package com.tokopedia.charts.config

import com.tokopedia.charts.common.ChartTooltip
import com.tokopedia.charts.config.annotation.ChartConfigDsl
import com.tokopedia.charts.model.XAxisConfigModel
import com.tokopedia.charts.model.YAxisConfigModel

/**
 * Created By @ilhamsuaib on 25/06/20
 */

@ChartConfigDsl
open class BaseChartConfig {

    protected var isTooltipEnabled: Boolean = true
    protected var isScaleXEnabled: Boolean = false
    protected var isPitchZoomEnabled: Boolean = false
    protected var isShowValueEnabled: Boolean = false
    protected var xAnimationDuration: Int = 0
    protected var yAnimationDuration: Int = 0
    protected var xAxis: XAxisConfigModel = XAxisConfig.getDefault()
    protected var yAxis: YAxisConfigModel = YAxisConfig.getDefault()
    protected var tooltip: ChartTooltip? = null

    fun tooltipEnabled(lambda: () -> Boolean) {
        isTooltipEnabled = lambda()
    }

    fun scaleEnabled(lambda: () -> Boolean) {
        isScaleXEnabled = lambda()
    }

    fun pitchZoomEnabled(lambda: () -> Boolean) {
        isPitchZoomEnabled = lambda()
    }

    fun showValueEnabled(lambda: () -> Boolean) {
        isShowValueEnabled = lambda()
    }

    fun xAnimationDuration(lambda: () -> Int) {
        xAnimationDuration = lambda()
    }

    fun yAnimationDuration(lambda: () -> Int) {
        yAnimationDuration = lambda()
    }

    fun xAxis(lambda: XAxisConfig.() -> Unit) {
        xAxis = XAxisConfig.create(lambda)
    }

    fun yAxis(lambda: YAxisConfig.() -> Unit) {
        yAxis = YAxisConfig.create(lambda)
    }

    fun setChartTooltip(tooltip: ChartTooltip) {
        this.tooltip = tooltip
    }
}