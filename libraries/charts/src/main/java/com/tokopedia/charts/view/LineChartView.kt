package com.tokopedia.charts.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.Utils
import com.tokopedia.charts.R
import com.tokopedia.charts.config.linechart.model.LeftAxisConfig
import com.tokopedia.charts.config.linechart.model.LegendConfig
import com.tokopedia.charts.config.linechart.model.LineChartConfig
import com.tokopedia.charts.config.linechart.model.XAxisConfig
import com.tokopedia.charts.model.LineChartEntry
import com.tokopedia.charts.model.AxisLabel
import com.tokopedia.charts.utils.XAxisLabelFormatter
import com.tokopedia.charts.utils.YAxisLabelFormatter
import com.tokopedia.kotlin.extensions.view.getResDrawable
import kotlinx.android.synthetic.main.view_line_chart.view.*

/**
 * Created By @ilhamsuaib on 24/06/20
 */

class LineChartView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    companion object {
        private const val UNDEFINED = -1
        private const val LINE_MODE_LINEAR = 0
        private const val LINE_MODE_CONTINUE = 1
    }

    private var chartConfig: LineChartConfig? = null

    private var chartLineWidth: Float = 1f

    private var chartLineColor: Int = Color.BLACK
    private var chartLineMode: Int = LINE_MODE_LINEAR
    private var chartFillColor: Int = Color.TRANSPARENT
    private var xAxisTextColor: Int = Color.BLACK
    private var yAxisTextColor: Int = Color.BLACK
    private var chartDotColor: Int = Color.BLACK
    private var fillDrawable: Int = UNDEFINED

    private var drawCircleHole: Boolean = false
    private var showChartDot: Boolean = false
    private var chartFillEnabled: Boolean = true
    private var showChartValue: Boolean = false
    private var showVerticalGrid: Boolean = true
    private var showHorizontalGrid: Boolean = true

    init {
        View.inflate(context, R.layout.view_line_chart, this)

        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.LineChartView)
        typedArray.let {
            chartLineWidth = it.getFloat(R.styleable.LineChartView_lcvLineWidth, chartLineWidth)

            chartLineColor = it.getColor(R.styleable.LineChartView_lcvLineColor, chartLineColor)
            chartLineMode = it.getColor(R.styleable.LineChartView_lcvLineMode, chartLineMode)
            chartFillColor = it.getColor(R.styleable.LineChartView_lcvFillColor, chartFillColor)
            xAxisTextColor = it.getColor(R.styleable.LineChartView_lcvXAxisTextColor, xAxisTextColor)
            yAxisTextColor = it.getColor(R.styleable.LineChartView_lcvYAxisTextColor, yAxisTextColor)
            chartDotColor = it.getColor(R.styleable.LineChartView_lcvChartDotColor, chartDotColor)
            fillDrawable = it.getResourceId(R.styleable.LineChartView_lcvFillDrawable, fillDrawable)

            drawCircleHole = it.getBoolean(R.styleable.LineChartView_lcvDrawCircleHole, drawCircleHole)
            showChartDot = it.getBoolean(R.styleable.LineChartView_lcvShowChartDot, showChartDot)
            chartFillEnabled = it.getBoolean(R.styleable.LineChartView_lcvFillEnabled, chartFillEnabled)
            showChartValue = it.getBoolean(R.styleable.LineChartView_lcvShowChartValue, showChartValue)
            showVerticalGrid = it.getBoolean(R.styleable.LineChartView_lcvShowVerticalGrid, showVerticalGrid)
            showHorizontalGrid = it.getBoolean(R.styleable.LineChartView_lcvShowHorizontalGrid, showHorizontalGrid)
        }
        typedArray.recycle()
    }

    fun init(config: LineChartConfig) {
        this.chartConfig = config

        with(lineChart) {
            axisRight.isEnabled = config.rightAxisConfig.isEnabled

            setupXAxis(xAxis, config.xAxisConfig)
            setupYAxis(axisLeft, config.leftAxisConfig)
            setupLegend(legend, config.legendConfig)

            description.isEnabled = config.isDescriptionEnabled

            setDrawMarkers(config.isDrawMarkersEnabled)
            setScaleEnabled(config.isScaleXEnabled)
            setPinchZoom(config.isPitchZoomEnabled)
        }

        setChartAnimation()
        setChartTooltip()
    }

    fun setData(chartEntry: List<LineChartEntry>) {
        val isDataSetExist = lineChart.data != null && lineChart.data.dataSetCount > 0

        val entries: List<Entry> = getLineChartData(chartEntry)

        setXAxisLabelFormatter(chartEntry)

        val dataSet: LineDataSet = if (isDataSetExist) {
            (lineChart.data.getDataSetByIndex(0) as LineDataSet).apply {
                values = entries
            }
        } else {
            LineDataSet(entries, "Data Set")
        }

        with(dataSet) {
            mode = when (chartLineMode) {
                LINE_MODE_LINEAR -> LineDataSet.Mode.LINEAR
                else -> LineDataSet.Mode.CUBIC_BEZIER
            }
            setDrawCircles(false)

            setDrawHorizontalHighlightIndicator(false)
            setDrawVerticalHighlightIndicator(false)

            //setup chart line
            lineWidth = chartLineWidth
            color = chartLineColor

            //setup chart fill color
            setDrawFilled(chartFillEnabled)
            if (this@LineChartView.fillDrawable != UNDEFINED && Utils.getSDKInt() >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                fillDrawable = context.getResDrawable(this@LineChartView.fillDrawable)
            } else {
                fillColor = chartFillColor
            }

            setDrawValues(showChartValue)

            //chart dot
            setDrawCircles(showChartDot)
            setCircleColor(chartDotColor)
            setDrawCircleHole(drawCircleHole)
        }

        lineChart.data = LineData(dataSet)

        val minY = entries.minBy { it.y }?.y ?: 0f
        lineChart.axisLeft.axisMinimum = minY
        lineChart.axisRight.axisMinimum = minY
    }

    fun setCustomYAxisLabel(labels: List<AxisLabel>) {
        val minY = labels.minBy { it.value }?.value ?: 0f
        lineChart.axisLeft.axisMinimum = minY
        lineChart.axisRight.axisMinimum = minY

        lineChart.axisLeft.run {
            setLabelCount(labels.size, true)
            valueFormatter = YAxisLabelFormatter(labels)
        }
    }

    fun invalidateChart() {
        lineChart.invalidate()
    }

    private fun setChartAnimation() {
        chartConfig?.let { config ->
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
    }

    private fun setChartTooltip() {
        chartConfig?.let {
            if (it.isDrawMarkersEnabled) {
                it.tooltip?.markerView?.chartView = lineChart
                lineChart.marker = it.tooltip?.markerView
            }
        }
    }

    private fun setXAxisLabelFormatter(entries: List<LineChartEntry>) {
        val labelsStr: List<String> = entries.map { it.xLabel }
        lineChart.xAxis.valueFormatter = XAxisLabelFormatter(labelsStr)

        if (labelsStr.size > 7) {
            lineChart.isScaleXEnabled = true
        } else {
            lineChart.xAxis.setLabelCount(labelsStr.size, true)
            lineChart.isScaleXEnabled = chartConfig?.isScaleXEnabled ?: false
        }

        lineChart.xAxis.axisMinimum = 0f
    }

    private fun getLineChartData(dataSet: List<LineChartEntry>): List<Entry> {
        return dataSet.mapIndexed { i, e ->
            Entry(i.toFloat(), e.yValue, e)
        }
    }

    private fun setupLegend(legend: Legend, config: LegendConfig) = with(legend) {
        isEnabled = config.isEnabled
    }

    private fun setupXAxis(axis: XAxis, config: XAxisConfig) = with(axis) {
        if (config.typeface != null) {
            typeface = config.typeface
        }
        position = config.getPosition()
        setDrawGridLines(showVerticalGrid)
        textColor = xAxisTextColor
    }

    private fun setupYAxis(axis: YAxis, config: LeftAxisConfig) = with(axis) {
        if (config.typeface != null) {
            typeface = config.typeface
        }
        textColor = yAxisTextColor
        setPosition(config.getPosition())
        setDrawGridLines(showHorizontalGrid)
    }
}