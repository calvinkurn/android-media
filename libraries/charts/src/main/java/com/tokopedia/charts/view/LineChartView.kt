package com.tokopedia.charts.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import com.tokopedia.charts.config.LineChartConfig
import com.tokopedia.charts.databinding.ViewLineChartBinding
import com.tokopedia.charts.model.LineChartConfigModel
import com.tokopedia.charts.model.LineChartData
import com.tokopedia.charts.model.LineChartEntry
import com.tokopedia.charts.renderer.EllipsizedXAxisRenderer

/**
 * Created By @ilhamsuaib on 24/06/20
 */

class LineChartView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    companion object {
        const val LINE_MODE_LINEAR = 0
        const val LINE_MODE_CURVE = 1
    }

    private var binding: ViewLineChartBinding? = null
    var config: LineChartConfigModel = LineChartConfig.getDefaultConfig()
        private set

    init {
        binding = ViewLineChartBinding.inflate(
            LayoutInflater.from(context), this, true
        ).apply {
            val xAxisRenderer = EllipsizedXAxisRenderer(
                lineChart.viewPortHandler,
                lineChart.xAxis,
                lineChart.getTransformer(YAxis.AxisDependency.LEFT)
            )
            lineChart.setXAxisRenderer(xAxisRenderer)
        }
    }

    fun init(mConfig: LineChartConfigModel? = null) {
        mConfig?.let {
            this.config = it
        }

        binding?.lineChart?.run {
            axisRight.isEnabled = false
            legend.isEnabled = false
            description.isEnabled = false

            setupXAxis()
            setupYAxis()

            setDrawMarkers(false)
            setScaleEnabled(config.isScaleXEnabled)
            setPinchZoom(config.isPitchZoomEnabled)
            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onNothingSelected() {

                }

                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    this@run.setDrawMarkers(config.isTooltipEnabled)
                }
            })
        }

        setChartAnimation()
        setChartTooltip()
    }

    fun setDataSets(vararg dataSet: LineChartData) {
        val dateSets: List<LineDataSet> = dataSet.map { data ->
            return@map getLineDataSet(data)
        }

        binding?.lineChart?.data = LineData(dateSets)
    }

    fun setDataSets(dataSets: List<LineChartData>) {
        dataSets.map { getLineDataSet(it) }
        binding?.lineChart?.data = LineData(dataSets.map { getLineDataSet(it) })
    }

    private fun getLineDataSet(data: LineChartData): LineDataSet {
        val entries: List<Entry> = getLineChartEntry(data.chartEntry)

        setXAxisLabelFormatter(data.chartEntry)
        setYAxisLabelFormatter()

        val lineDataSet = LineDataSet(entries, "Data Set")

        with(lineDataSet) {
            mode = when (config.chartLineMode) {
                LINE_MODE_CURVE -> LineDataSet.Mode.CUBIC_BEZIER
                else -> LineDataSet.Mode.LINEAR
            }

            setDrawHorizontalHighlightIndicator(false)
            setDrawVerticalHighlightIndicator(false)

            //setup chart line
            lineWidth = data.config.lineWidth
            color = data.config.lineColor
            if (data.config.isLineDashed) {
                enableDashedLine(20f, 10f, 0f)
            }

            //setup chart fill color
            setDrawFilled(data.config.drawFillEnabled)
            if (data.config.fillDrawable != null && Utils.getSDKInt() >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                fillDrawable = data.config.fillDrawable
            } else {
                fillColor = data.config.fillColor
            }

            setDrawValues(config.isShowValueEnabled)

            //chart dot
            setDrawCircles(config.isChartDotEnabled)
            setCircleColor(config.chartDotColor)
            setDrawCircleHole(config.isChartDotHoleEnabled)
        }

        return lineDataSet
    }

    fun invalidateChart() {
        binding?.lineChart?.invalidate()
    }

    private fun setChartAnimation() {
        binding?.lineChart?.run {
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
                config.tooltip?.markerView?.chartView = lineChart
                lineChart.marker = config.tooltip?.markerView
            }
        }
    }

    private fun setXAxisLabelFormatter(entries: List<LineChartEntry>) {
        val xAxisConfig = config.xAxisConfig
        val labelsStr: List<String> = entries.map { it.xLabel }
        binding?.run {
            lineChart.xAxis?.valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return xAxisConfig.labelFormatter.getAxisLabel(value)
                }
            }

            if (labelsStr.size > 7) {
                lineChart.isScaleXEnabled = true
            } else {
                lineChart.xAxis.setLabelCount(labelsStr.size, true)
                lineChart.isScaleXEnabled = config.isScaleXEnabled
            }

            lineChart.xAxis.axisMinimum = xAxisConfig.axisMinimum
        }
    }

    private fun setYAxisLabelFormatter() {
        val yAxisConfig = config.yAxisConfig
        binding?.lineChart?.axisLeft?.run {
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

    private fun setupXAxis() = binding?.lineChart?.xAxis?.run {
        val axisConfig = config.xAxisConfig
        axisConfig.typeface?.let { tf ->
            typeface = tf
        }
        isEnabled = axisConfig.isEnabled
        setDrawGridLines(axisConfig.isGridEnabled)
        setDrawLabels(axisConfig.isLabelEnabled)
        position = axisConfig.getLabelPosition()
    }

    private fun setupYAxis() = binding?.lineChart?.axisLeft?.run {
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