package com.tokopedia.charts.config.piechart

import android.graphics.Color
import com.tokopedia.charts.config.piechart.annotation.PieChartDsl
import com.tokopedia.charts.config.piechart.model.DonutStyleConfig
import com.tokopedia.charts.config.piechart.model.PieChartConfig

/**
 * Created By @ilhamsuaib on 07/07/20
 */

@PieChartDsl
class PieChartConfigBuilder {

    companion object {
        fun getDefaultConfig(): PieChartConfig = create { }

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
    var donutStyleConfig: DonutStyleConfig = DonutStyleConfigBuilder.create { }
        private set

    fun build(): PieChartConfig {
        return PieChartConfig(
                entryLabelColor = entryLabelColor,
                entryLabelTextSize = entryLabelTextSize,
                xAnimationDuration = xAnimationDuration,
                yAnimationDuration = yAnimationDuration,
                sliceSpaceWidth = sliceSpaceWidth,
                isHalfChart = isHalfChart,
                rotationEnabled = rotationEnabled,
                highlightPerTapEnabled = highlightPerTapEnabled,
                animationEnabled = animationEnabled,
                showXValueEnabled = showXValueEnabled,
                showYValueEnabled = showYValueEnabled,
                touchEnabled = touchEnabled,
                legendEnabled = legendEnabled,
                donutStyleConfig = donutStyleConfig
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

    fun donutChart(lambda: DonutStyleConfigBuilder.() -> Unit) {
        this.donutStyleConfig = DonutStyleConfigBuilder.create(lambda)
    }
}

@PieChartDsl
class DonutStyleConfigBuilder {

    companion object {
        fun create(lambda: DonutStyleConfigBuilder.() -> Unit): DonutStyleConfig {
            return DonutStyleConfigBuilder().apply(lambda).build()
        }
    }

    var isEnabled: Boolean = false
        private set
    var isCurveEnabled: Boolean = false
        private set
    var holeRadius: Float = 50f
        private set

    fun build(): DonutStyleConfig {
        return DonutStyleConfig(
                isEnabled = isEnabled,
                isCurveEnabled = isCurveEnabled,
                holeRadius = holeRadius
        )
    }

    /**
     * set this to true to draw the pie center empty. Default false
     */
    fun enabled(lambda: () -> Boolean) {
        this.isEnabled = lambda()
    }

    /**
     * sets whether to draw slices in a curved fashion. Default false
     */
    fun curveEnabled(lambda: () -> Boolean) {
        this.isCurveEnabled = lambda()
    }

    /**
     * sets the radius of the hole in the center of the piechart in percent of
     * the maximum radius (max = the radius of the whole chart), default 50%.
     */
    fun holeRadiusPercent(lambda: () -> Float) {
        this.holeRadius = lambda()
    }
}