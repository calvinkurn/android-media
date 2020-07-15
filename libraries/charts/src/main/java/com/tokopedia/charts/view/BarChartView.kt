package com.tokopedia.charts.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.tokopedia.charts.R
import com.tokopedia.charts.common.ChartColor
import com.tokopedia.charts.config.barchart.BarChartConfigBuilder
import com.tokopedia.charts.config.barchart.model.BarChartConfig
import com.tokopedia.charts.model.AxisLabel
import com.tokopedia.charts.model.BarChartData
import com.tokopedia.charts.utils.RoundedBarChartRenderer
import com.tokopedia.charts.utils.XAxisLabelFormatter
import com.tokopedia.charts.utils.YAxisLabelFormatter
import com.tokopedia.kotlin.extensions.view.orZero
import kotlinx.android.synthetic.main.view_bar_chart.view.*

/**
 * Created By @ilhamsuaib on 09/07/20
 */

class BarChartView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.view_bar_chart, this)
    }

    var config: BarChartConfig = BarChartConfigBuilder.getDefaultConfig()
        private set

    fun init(mConfig: BarChartConfig? = null) {
        if (mConfig != null) {
            this.config = mConfig
        }

        with(barChart) {
            if (config.isRoundedBar) {
                renderer = RoundedBarChartRenderer(this, animator, viewPortHandler, config.borderRadius)
            }

            setupXAxis()
            setupYAxis()

            description.isEnabled = false
            legend.isEnabled = false
            axisRight.isEnabled = false

            setScaleEnabled(false)
            setPinchZoom(false)
        }

        setChartTooltip()
    }

    fun setData(data: BarChartData) {
        setXAxisLabelFormatter(data.xAxisLabels)
        val barDataSets = mutableListOf<BarDataSet>()
        data.metrics.forEach { metric ->
            val barEntries: List<BarEntry> = metric.values.mapIndexed { i, value ->
                BarEntry(i.toFloat(), value.value.toFloat(), value)
            }
            val dataSet = BarDataSet(barEntries, metric.title)
            dataSet.color = getColor(metric.barHexColor)
            dataSet.highLightAlpha = config.highLightAlpha
            dataSet.setDrawValues(config.showBarValueEnabled)

            barDataSets.add(dataSet)
        }

        val barData = BarData(barDataSets.toList())
        barChart.data = barData

        if (data.metrics.size > 1) {
            showMultiBar(data)
        }

        barChart.axisLeft.axisMinimum = data.yAxis.minBy { it.value }?.value.orZero()
        barChart.axisLeft.run {
            setLabelCount(data.yAxis.size, true)
            valueFormatter = YAxisLabelFormatter(data.yAxis)
            granularity = 1f
        }
    }

    fun invalidateChart() {
        barChart.invalidate()
    }

    private fun setChartTooltip() {
        if (config.drawMarkersEnabled) {
            val tooltip = config.tooltip
            tooltip?.markerView?.chartView = barChart
            barChart?.marker = tooltip?.markerView
        }
    }

    private fun setXAxisLabelFormatter(labels: List<AxisLabel>) {
        val labelsStr = labels.map { it.valueFmt }
        barChart.xAxis.valueFormatter = XAxisLabelFormatter(labelsStr)
        barChart.isScaleXEnabled = labelsStr.size > 7
    }

    private fun getColor(barHexColor: String): Int {
        val hexColor = if (barHexColor.isBlank()) ChartColor.DEFAULT_COLOR else barHexColor
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
            setDrawGridLines(axisConfig.showGridEnabled)
            spaceTop = axisConfig.spaceTop
        }
    }

    private fun setupXAxis() {
        val axisConfig = config.xAxisConfig
        with(barChart.xAxis) {
            setDrawLabels(axisConfig.showLabelEnabled)
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            setDrawGridLines(axisConfig.showGridEnabled)
        }
    }
}