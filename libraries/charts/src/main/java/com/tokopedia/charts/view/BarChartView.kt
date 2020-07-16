package com.tokopedia.charts.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.tokopedia.charts.R
import com.tokopedia.charts.common.ChartColor
import com.tokopedia.charts.config.BarChartConfig
import com.tokopedia.charts.model.AxisLabel
import com.tokopedia.charts.model.BarChartConfigModel
import com.tokopedia.charts.model.BarChartData
import com.tokopedia.charts.common.utils.RoundedBarChartRenderer
import com.tokopedia.charts.common.utils.XAxisLabelFormatter
import com.tokopedia.charts.common.utils.YAxisLabelFormatter
import com.tokopedia.kotlin.extensions.view.orZero
import kotlinx.android.synthetic.main.view_bar_chart.view.*

/**
 * Created By @ilhamsuaib on 09/07/20
 */

class BarChartView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.view_bar_chart, this)
    }

    var config: BarChartConfigModel = BarChartConfig.getDefaultConfig()
        private set

    fun init(mConfig: BarChartConfigModel? = null) {
        mConfig?.let {
            this.config = it
        }

        with(barChart) {
            if (config.isRoundedBarEnabled) {
                renderer = RoundedBarChartRenderer(this, animator, viewPortHandler, config.barBorderRadius)
            }

            setupXAxis()
            setupYAxis()

            description.isEnabled = false
            legend.isEnabled = false
            axisRight.isEnabled = false

            setDrawMarkers(config.isTooltipEnabled)
            setScaleEnabled(config.isScaleXEnabled)
            setPinchZoom(config.isPitchZoomEnabled)
        }

        setChartAnimation()
        setChartTooltip()
    }

    fun setData(data: BarChartData) {
        setXAxisLabelFormatter(data.xAxisLabels)
        setYAxisLabelFormatter(data.yAxis)
        val barDataSets = mutableListOf<BarDataSet>()
        data.metrics.forEach { metric ->
            val barEntries: List<BarEntry> = metric.values.mapIndexed { i, value ->
                BarEntry(i.toFloat(), value.value.toFloat(), value)
            }
            val dataSet = BarDataSet(barEntries, metric.title)
            dataSet.color = getColor(metric.barHexColor)
            dataSet.highLightAlpha = config.highLightAlpha
            dataSet.setDrawValues(config.isShowValueEnabled)

            barDataSets.add(dataSet)
        }

        val barData = BarData(barDataSets.toList())
        barChart.data = barData

        if (data.metrics.size > 1) {
            showMultiBar(data)
        }
    }

    fun invalidateChart() {
        barChart.invalidate()
    }

    private fun setYAxisLabelFormatter(yAxis: List<AxisLabel>) {
        barChart.axisLeft.axisMinimum = yAxis.minBy { it.value }?.value.orZero()
        barChart.axisLeft.run {
            setLabelCount(yAxis.size, true)
            valueFormatter = YAxisLabelFormatter(yAxis)
        }
    }

    private fun setChartAnimation() {
        with(barChart) {
            when {
                (config.xAnimationDuration > 0 && config.yAnimationDuration > 0) -> {
                    animateXY(config.xAnimationDuration, config.yAnimationDuration)
                }
                config.xAnimationDuration > 0 -> {
                    animateX(config.xAnimationDuration)
                }
                config.yAnimationDuration > 0 -> {
                    animateX(config.yAnimationDuration)
                }
            }
        }
    }

    private fun setChartTooltip() {
        if (config.isTooltipEnabled) {
            val tooltip = config.tooltip
            tooltip?.markerView?.chartView = barChart
            barChart?.marker = tooltip?.markerView
        }
    }

    private fun setXAxisLabelFormatter(labels: List<AxisLabel>) {
        val labelsStr = labels.map { it.valueFmt }
        barChart.xAxis.valueFormatter = XAxisLabelFormatter(labelsStr)
        if (labelsStr.size > 7) {
            barChart.isScaleXEnabled = true
        } else {
            barChart.isScaleXEnabled = config.isScaleXEnabled
        }
    }

    private fun getColor(barHexColor: String): Int {
        val hexColor = if (barHexColor.isBlank()) ChartColor.DEFAULT_BAR_COLOR else barHexColor
        return Color.parseColor(hexColor)
    }

    private fun showMultiBar(data: BarChartData) {
        val startValue = data.xAxisLabels.firstOrNull()?.value.orZero()
        val groupCount = data.metrics.size

        val groupSpace = 0.08f
        val barSpace = 0.03f // x4 DataSet
        val barWidth = 0.2f // 0.2f -> x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        // specify the width each bar should have
        barChart.barData.barWidth = barWidth
        barChart.xAxis.axisMinimum = startValue
        barChart.xAxis.axisMaximum = startValue + barChart.barData.getGroupWidth(groupSpace, barSpace) * groupCount
        barChart.groupBars(startValue, groupSpace, barSpace)
    }

    private fun setupYAxis() {
        val axisConfig = config.yAxisConfig
        with(barChart.axisLeft) {
            axisConfig.typeface?.let { tf ->
                typeface = tf
            }
            isEnabled = axisConfig.isEnabled
            spaceTop = axisConfig.spaceTop
            setDrawGridLines(axisConfig.isGridEnabled)
            setDrawLabels(axisConfig.isLabelEnabled)
            setPosition(axisConfig.getLabelPosition())
        }
    }

    private fun setupXAxis() {
        val axisConfig = config.xAxisConfig
        with(barChart.xAxis) {
            axisConfig.typeface?.let { tf ->
                typeface = tf
            }
            isEnabled = axisConfig.isEnabled
            position = axisConfig.getLabelPosition()
            granularity = 1f
            setDrawLabels(axisConfig.isLabelEnabled)
            setDrawGridLines(axisConfig.isGridEnabled)
        }
    }
}