package com.tokopedia.charts.config

import android.graphics.Color
import com.tokopedia.charts.config.annotation.PieChartDsl
import com.tokopedia.charts.model.DonutStyleConfigModel
import com.tokopedia.charts.model.PieChartConfigModel
import com.tokopedia.charts.view.PieChartView

/**
 * Created By @ilhamsuaib on 07/07/20
 */

@PieChartDsl
class PieChartConfig {

    companion object {
        fun getDefaultConfig(): PieChartConfigModel = create { }

        fun create(lambda: PieChartConfig.() -> Unit) = PieChartConfig().apply(lambda).build()
    }

    private var entryLabelColor: Int = Color.BLACK
    private var entryLabelTextSize: Float = 12f
    private var pieChartWidth: Int = PieChartView.SIZE_UNDEFINED
    private var pieChartHeight: Int = PieChartView.SIZE_UNDEFINED
    private var yAnimationDuration: Int = 0
    private var xAnimationDuration: Int = 0
    private var sliceSpaceWidth: Float = 0f
    private var isHalfChart: Boolean = false
    private var rotationEnabled: Boolean = false
    private var highlightPerTapEnabled: Boolean = false
    private var animationEnabled: Boolean = true
    private var showXValueEnabled: Boolean = false
    private var showYValueEnabled: Boolean = false
    private var touchEnabled: Boolean = false
    private var legendEnabled: Boolean = true
    private var donutStyleConfig: DonutStyleConfigModel = DonutStyleConfig.getDefault()

    internal fun build(): PieChartConfigModel {
        return PieChartConfigModel(
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
                donutStyleConfig = donutStyleConfig,
                pieChartWidth = pieChartWidth,
                pieChartHeight = pieChartHeight
        )
    }

    fun entryLabelColor(lambda: () -> Int) {
        this.entryLabelColor = lambda()
    }

    fun entryLabelTextSize(lambda: () -> Float) {
        this.entryLabelTextSize = lambda()
    }

    fun pieChartWidth(lambda: () -> Int) {
        this.pieChartWidth = lambda()
    }

    fun pieChartHeight(lambda: () -> Int) {
        this.pieChartHeight = lambda()
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

    fun donutChart(lambda: DonutStyleConfig.() -> Unit) {
        this.donutStyleConfig = DonutStyleConfig.create(lambda)
    }
}