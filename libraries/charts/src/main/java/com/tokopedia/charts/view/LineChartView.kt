package com.tokopedia.charts.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.Utils
import com.tokopedia.charts.R
import com.tokopedia.charts.config.LineChartConfig
import com.tokopedia.charts.model.LineChartConfigModel
import com.tokopedia.charts.model.LineChartData
import com.tokopedia.charts.model.LineChartEntry
import com.tokopedia.charts.common.utils.XAxisLabelFormatter
import kotlinx.android.synthetic.main.view_line_chart.view.*

/**
 * Created By @ilhamsuaib on 24/06/20
 */

class LineChartView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    companion object {
        const val LINE_MODE_LINEAR = 0
        const val LINE_MODE_CURVE = 1
    }

    var config: LineChartConfigModel = LineChartConfig.getDefaultConfig()
        private set

    init {
        View.inflate(context, R.layout.view_line_chart, this)
    }

    fun init(mConfig: LineChartConfigModel? = null) {
        mConfig?.let {
            this.config = it
        }

        with(lineChart) {
            axisRight.isEnabled = false
            legend.isEnabled = false
            description.isEnabled = false

            setupXAxis()
            setupYAxis()

            setDrawMarkers(config.isTooltipEnabled)
            setScaleEnabled(config.isScaleXEnabled)
            setPinchZoom(config.isPitchZoomEnabled)
        }

        setChartAnimation()
        setChartTooltip()
    }

    fun setData(chartData: LineChartData) {
        val entries: List<Entry> = getLineChartEntry(chartData.chartEntry)

        setXAxisLabelFormatter(chartData.chartEntry)
        setYAxisLabelFormatter()

        val dataSet = LineDataSet(entries, "Data Set")

        with(dataSet) {
            mode = when (config.chartLineMode) {
                LINE_MODE_CURVE -> LineDataSet.Mode.CUBIC_BEZIER
                else -> LineDataSet.Mode.LINEAR
            }
            setDrawCircles(false)

            setDrawHorizontalHighlightIndicator(false)
            setDrawVerticalHighlightIndicator(false)

            //setup chart line
            lineWidth = config.chartLineWidth
            color = config.chartLineColor

            //setup chart fill color
            setDrawFilled(config.isChartFillEnabled)
            if (config.fillDrawable != null && Utils.getSDKInt() >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                fillDrawable = config.fillDrawable
            } else {
                fillColor = config.chartFillColor
            }

            setDrawValues(config.isShowValueEnabled)

            //chart dot
            setDrawCircles(config.isChartDotEnabled)
            setCircleColor(config.chartDotColor)
            setDrawCircleHole(config.isChartDotHoleEnabled)
        }

        lineChart.data = LineData(dataSet)
    }

    fun invalidateChart() {
        lineChart.invalidate()
    }

    private fun setChartAnimation() {
        with(lineChart) {
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
            config.tooltip?.markerView?.chartView = lineChart
            lineChart.marker = config.tooltip?.markerView
        }
    }

    private fun setXAxisLabelFormatter(entries: List<LineChartEntry>) {
        val xAxisConfig = config.xAxisConfig
        val labelsStr: List<String> = entries.map { it.xLabel }
        lineChart.xAxis.valueFormatter = XAxisLabelFormatter(labelsStr)

        if (labelsStr.size > 7) {
            lineChart.isScaleXEnabled = true
        } else {
            lineChart.xAxis.setLabelCount(labelsStr.size, true)
            lineChart.isScaleXEnabled = config.isScaleXEnabled
        }

        lineChart.xAxis.axisMinimum = xAxisConfig.axisMinimum
    }

    private fun setYAxisLabelFormatter() {
        val yAxisConfig = config.yAxisConfig
        lineChart.axisLeft.run {
            axisMinimum = yAxisConfig.axisMinimum
            setLabelCount(yAxisConfig.labelCount, true)
            valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return yAxisConfig.labelFormatter.getAxisLabel(value)
                }
            }
        }
    }

    private fun getLineChartEntry(dataSet: List<LineChartEntry>): List<Entry> {
        return dataSet.mapIndexed { i, e ->
            Entry(i.toFloat(), e.yValue, e)
        }
    }

    private fun setupXAxis() = with(lineChart.xAxis) {
        val axisConfig = config.xAxisConfig
        axisConfig.typeface?.let { tf ->
            typeface = tf
        }
        isEnabled = axisConfig.isEnabled
        setDrawGridLines(axisConfig.isGridEnabled)
        setDrawLabels(axisConfig.isLabelEnabled)
        position = axisConfig.getLabelPosition()
    }

    private fun setupYAxis() = with(lineChart.axisLeft) {
        val axisConfig = config.yAxisConfig
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