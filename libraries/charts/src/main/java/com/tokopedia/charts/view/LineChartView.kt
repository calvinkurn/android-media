package com.tokopedia.charts.view

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.tokopedia.charts.R
import com.tokopedia.charts.config.linechart.model.LeftAxisConfig
import com.tokopedia.charts.config.linechart.model.LegendConfig
import com.tokopedia.charts.config.linechart.model.LineChartConfig
import com.tokopedia.charts.config.linechart.model.XAxisConfig
import com.tokopedia.charts.model.LineChartEntry
import com.tokopedia.charts.model.YAxisLabel
import com.tokopedia.charts.utils.XAxisLabelFormatter
import com.tokopedia.charts.utils.YAxisLabelFormatter
import com.tokopedia.kotlin.extensions.view.getResColor
import kotlinx.android.synthetic.main.view_line_chart.view.*

/**
 * Created By @ilhamsuaib on 24/06/20
 */

class LineChartView : LinearLayout {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var graphConfig: LineChartConfig? = null

    var typeface: Typeface? = null
        set(value) {
            field = value
            lineChart.xAxis.typeface = value
            lineChart.axisLeft.typeface = value
        }

    @ColorRes
    var xAxisTextColor: Int
        set(value) {
            field = value
            lineChart.xAxis.textColor = value
        }

    @ColorRes
    var yAxisTextColor: Int
        set(value) {
            field = value
            lineChart.axisLeft.textColor = value
        }

    init {
        View.inflate(context, R.layout.view_line_chart, this)

        xAxisTextColor = context.getResColor(R.color.Neutral_N700_96)
        yAxisTextColor = context.getResColor(R.color.Neutral_N700_96)
    }

    fun init(config: LineChartConfig) {
        this.graphConfig = config
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.assets, "SFProText-Regular.ttf")
        }

        with(lineChart) {
            axisRight.isEnabled = config.rightAxisConfig.isEnabled

            setupXAxis(xAxis, config.xAxisConfig)
            setupYAxis(axisLeft, config.leftAxisConfig)
            setupLegend(legend, config.legendConfig)

            description.isEnabled = config.isDescriptionEnabled

            setDrawMarkers(config.isDrawMarkersEnabled)
            setScaleEnabled(config.isScaleXEnabled)
            setPinchZoom(config.isPitchZoomEnabled)

            animateXY(200, 200)
        }

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
            mode = LineDataSet.Mode.LINEAR
            setDrawCircles(false)
            setDrawFilled(true)
            setDrawHorizontalHighlightIndicator(false)
            setDrawVerticalHighlightIndicator(false)
            //setup chart line
            lineWidth = 1.8f
            color = context.getResColor(R.color.line_chart_line_color_4fba68)
            //setup fill
            fillColor = context.getResColor(R.color.line_chart_fill_color_35d6ffde)
            setDrawValues(false)
        }

        lineChart.data = LineData(dataSet)

        val minY = entries.minBy { it.y }?.y ?: 0f
        lineChart.axisLeft.axisMinimum = minY
        lineChart.axisRight.axisMinimum = minY
    }

    fun setCustomYAxisLabel(labels: List<YAxisLabel>) {
        val minY = labels.minBy { it.yValue }?.yValue ?: 0f
        lineChart.axisLeft.axisMinimum = minY
        lineChart.axisRight.axisMinimum = minY

        lineChart.axisLeft.run {
            setLabelCount(labels.size, true)
            valueFormatter = YAxisLabelFormatter(labels)
        }
    }

    private fun setChartTooltip() {
        graphConfig?.let {
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
            lineChart.isScaleXEnabled = graphConfig?.isScaleXEnabled ?: false
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

    fun invalidateChart() {
        lineChart.invalidate()
    }

    private fun setupXAxis(axis: XAxis, config: XAxisConfig) = with(axis) {
        if (config.typeface != null) {
            typeface = config.typeface
        } else if (typeface != null) {
            typeface = this@LineChartView.typeface
        }

        textColor = config.textColor
        position = config.getPosition()
        setDrawGridLines(config.isDrawGridLines)
    }

    private fun setupYAxis(axis: YAxis, config: LeftAxisConfig) = with(axis) {
        if (config.typeface != null) {
            typeface = config.typeface
        } else if (typeface != null) {
            typeface = this@LineChartView.typeface
        }

        textColor = config.textColor
        setPosition(config.getPosition())
        setDrawGridLines(config.isDrawGridLines)
    }
}