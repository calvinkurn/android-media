package com.tokopedia.charts.config.piechart

import android.graphics.Color
import com.tokopedia.charts.config.piechart.annotation.PieChartDsl
import com.tokopedia.charts.config.piechart.annotation.PieChartStyle
import com.tokopedia.charts.config.piechart.model.PieChartConfig

/**
 * Created By @ilhamsuaib on 07/07/20
 */

@PieChartDsl
class PieChartConfigBuilder {

    companion object {
        fun getDefaultConfig(): PieChartConfig = create {  }

        fun create(lambda: PieChartConfigBuilder.() -> Unit) = PieChartConfigBuilder().apply(lambda).build()
    }

    var entryLabelColor: Int = Color.BLACK
        private set
    var entryLabelTextSize: Float = 12f
        private set
    var yAnimationDuration: Int = 0
        private set
    var xAnimationDuration: Int = 0
        private set

    @PieChartStyle
    var chartStyle: Int = PieChartStyle.DEFAULT
        private set
    var sliceSpaceWidth: Float = 0f
        private set
    var isHalfChart: Boolean = false
        private set
    var rotationEnabled: Boolean = false
        private set
    var highlightPerTapEnabled: Boolean = false
        private set
    var animationEnabled: Boolean = true
        private set
    var showXValueEnabled: Boolean = false
        private set
    var showYValueEnabled: Boolean = false
        private set
    var touchEnabled: Boolean = false
        private set
    var legendEnabled: Boolean = true
        private set

    fun build(): PieChartConfig {
        return PieChartConfig(
                entryLabelColor = entryLabelColor,
                entryLabelTextSize = entryLabelTextSize,
                xAnimationDuration = xAnimationDuration,
                yAnimationDuration = yAnimationDuration,
                chartStyle = chartStyle,
                sliceSpaceWidth = sliceSpaceWidth,
                isHalfChart = isHalfChart,
                rotationEnabled = rotationEnabled,
                highlightPerTapEnabled = highlightPerTapEnabled,
                animationEnabled = animationEnabled,
                showXValueEnabled = showXValueEnabled,
                showYValueEnabled = showYValueEnabled,
                touchEnabled = touchEnabled,
                legendEnabled = legendEnabled
        )
    }

    fun entryLabelColor(lambda: () -> Int) {
        this.entryLabelColor = lambda()
    }

    fun entryLabelTextSize(lambda: () -> Float) {
        this.entryLabelTextSize = lambda()
    }

    fun xAnimationDuration(lambda: () -> Int) {
        this.xAnimationDuration = lambda()
    }

    fun yAnimationDuration(lambda: () -> Int) {
        this.yAnimationDuration = lambda()
    }

    fun chartStyle(lambda: () -> Int) {
        this.chartStyle = lambda()
    }

    fun sliceSpaceWidth(lambda: () -> Float) {
        this.sliceSpaceWidth = lambda()
    }

    fun isHalfChart(lambda: () -> Boolean) {
        this.isHalfChart = lambda()
    }

    fun rotationEnabled(lambda: () -> Boolean) {
        this.rotationEnabled = lambda()
    }

    fun highlightPerTapEnabled(lambda: () -> Boolean) {
        this.highlightPerTapEnabled = lambda()
    }

    fun animationEnabled(lambda: () -> Boolean) {
        this.animationEnabled = lambda()
    }

    fun showXValueEnabled(lambda: () -> Boolean) {
        this.showXValueEnabled = lambda()
    }

    fun showYValueEnabled(lambda: () -> Boolean) {
        this.showYValueEnabled = lambda()
    }

    fun touchEnabled(lambda: () -> Boolean) {
        this.touchEnabled = lambda()
    }

    fun legendEnabled(lambda: () -> Boolean) {
        this.legendEnabled = lambda()
    }
}