package com.tokopedia.charts.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.tokopedia.charts.common.ChartColor
import com.tokopedia.charts.common.utils.RoundedBarChartRenderer
import com.tokopedia.charts.config.BarChartConfig
import com.tokopedia.charts.databinding.ViewBarChartBinding
import com.tokopedia.charts.model.AxisLabel
import com.tokopedia.charts.model.BarChartConfigModel
import com.tokopedia.charts.model.BarChartData
import com.tokopedia.charts.model.StackedBarChartData
import com.tokopedia.charts.renderer.EllipsizedXAxisRenderer
import com.tokopedia.kotlin.extensions.view.orZero

/**
 * Created By @ilhamsuaib on 09/07/20
 */

class BarChartView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var binding: ViewBarChartBinding? = null

    init {
        binding = ViewBarChartBinding.inflate(
            LayoutInflater.from(context), this, true
        ).apply {
            val xAxisRenderer = EllipsizedXAxisRenderer(
                barChart.viewPortHandler,
                barChart.xAxis,
                barChart.getTransformer(YAxis.AxisDependency.LEFT)
            )
            barChart.setXAxisRenderer(xAxisRenderer)
        }
    }

    var config: BarChartConfigModel = BarChartConfig.getDefaultConfig()
        private set

    fun init(mConfig: BarChartConfigModel? = null) {
        mConfig?.let {
            this.config = it
        }

        binding?.barChart?.run {
            if (config.isRoundedBarEnabled) {
                renderer =
                    RoundedBarChartRenderer(this, animator, viewPortHandler, config.barBorderRadius)
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
        setYAxisLabelFormatter()
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
        binding?.barChart?.data = barData

        if (data.metrics.size > 1) {
            showMultiBar(data)
        }
    }

    fun setData(data: StackedBarChartData) {
        setXAxisLabelFormatter(data.xAxisLabels)
        setYAxisLabelFormatter()
        val barDataSets = mutableListOf<BarDataSet>()

        data.metrics.forEach { metric ->
            val barEntries: List<BarEntry> = metric.values.mapIndexed { i, value ->
                BarEntry(i.toFloat(), value.values.map { it.value.toFloat() }.toFloatArray(), value)
            }
            val dataSet = BarDataSet(barEntries, metric.title)
            dataSet.colors = getColors()
            dataSet.highLightAlpha = config.highLightAlpha
            dataSet.setDrawValues(config.isShowValueEnabled)

            barDataSets.add(dataSet)
        }

        val barData = BarData(barDataSets.toList())
        binding?.barChart?.data = barData

        if (data.metrics.size > 1) {
            showMultiBar(data)
        }
    }

    fun invalidateChart() {
        binding?.barChart?.invalidate()
    }

    private fun setYAxisLabelFormatter() {
        val yAxisConfig = config.yAxisConfig
        binding?.barChart?.axisLeft?.run {
            axisMinimum = yAxisConfig.axisMinimum
            setLabelCount(config.yAxisConfig.labelCount, true)
            valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return config.yAxisConfig.labelFormatter.getAxisLabel(value)
                }
            }
        }
    }

    private fun setChartAnimation() {
        binding?.barChart?.run {
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
            binding?.run {
                val tooltip = config.tooltip
                tooltip?.markerView?.chartView = barChart
                barChart.marker = tooltip?.markerView
            }
        }
    }

    private fun setXAxisLabelFormatter(labels: List<AxisLabel>) {
        val labelsStrs = labels.map { it.valueFmt }
        val xAxisConfig = config.xAxisConfig

        binding?.barChart?.xAxis?.run {
            valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return xAxisConfig.labelFormatter.getAxisLabel(value)
                }
            }
        }

        if (labelsStrs.size > 7) {
            binding?.barChart?.isScaleXEnabled = true
        } else {
            binding?.barChart?.isScaleXEnabled = config.isScaleXEnabled
        }
    }

    private fun getColor(barHexColor: String): Int {
        val hexColor = if (barHexColor.isBlank()) ChartColor.DMS_DEFAULT_BAR_COLOR else barHexColor
        return Color.parseColor(hexColor)
    }

    private fun getColors(): List<Int> {
        return listOf(
            Color.parseColor("#4FE397"),
            Color.parseColor("#00AA5B")
        )
    }

    private fun showMultiBar(data: BarChartData) {
        val startValue = data.xAxisLabels.firstOrNull()?.value.orZero()
        val groupCount = data.metrics.size

        val groupSpace = 0.08f
        val barSpace = 0.03f // x4 DataSet
        val barWidth = 0.2f // 0.2f -> x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        // specify the width each bar should have
        binding?.run {
            barChart.barData.barWidth = barWidth
            barChart.xAxis.axisMinimum = startValue
            barChart.xAxis.axisMaximum =
                startValue + barChart.barData.getGroupWidth(groupSpace, barSpace) * groupCount
            barChart.groupBars(startValue, groupSpace, barSpace)
        }
    }

    private fun showMultiBar(data: StackedBarChartData) {
        val startValue = data.xAxisLabels.firstOrNull()?.value.orZero()
        val groupCount = data.metrics.size

        val groupSpace = 0.08f
        val barSpace = 0.03f // x4 DataSet
        val barWidth = 0.2f // 0.2f -> x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        // specify the width each bar should have
        binding?.run {
            barChart.barData.barWidth = barWidth
            barChart.xAxis.axisMinimum = startValue
            barChart.xAxis.axisMaximum =
                startValue + barChart.barData.getGroupWidth(groupSpace, barSpace) * groupCount
            barChart.groupBars(startValue, groupSpace, barSpace)
        }
    }

    private fun setupYAxis() {
        val axisConfig = config.yAxisConfig
        binding?.barChart?.axisLeft?.run {
            axisConfig.typeface?.let { tf ->
                typeface = tf
            }
            isEnabled = axisConfig.isEnabled
            spaceTop = axisConfig.spaceTop
            setDrawGridLines(axisConfig.isGridEnabled)
            setDrawLabels(axisConfig.isLabelEnabled)
            setPosition(axisConfig.getLabelPosition())
            textColor = axisConfig.textColor
        }
    }

    private fun setupXAxis() {
        val axisConfig = config.xAxisConfig
        binding?.barChart?.xAxis?.run {
            axisConfig.typeface?.let { tf ->
                typeface = tf
            }
            isEnabled = axisConfig.isEnabled
            position = axisConfig.getLabelPosition()
            granularity = 1f
            setDrawLabels(axisConfig.isLabelEnabled)
            setDrawGridLines(axisConfig.isGridEnabled)
            textColor = axisConfig.textColor
        }
    }
}
