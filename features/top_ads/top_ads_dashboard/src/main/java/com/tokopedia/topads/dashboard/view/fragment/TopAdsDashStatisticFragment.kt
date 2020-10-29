package com.tokopedia.topads.dashboard.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.db.williamchart.model.TooltipModel
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.charts.common.ChartTooltip
import com.tokopedia.charts.config.LineChartConfig
import com.tokopedia.charts.model.*
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.Cell
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.Summary
import com.tokopedia.topads.dashboard.data.utils.ChartXAxisLabelFormatter
import kotlinx.android.synthetic.main.topads_graph_tooltip.view.*
import kotlinx.android.synthetic.main.topads_statistics_graph_fragment.*
import java.text.SimpleDateFormat

/**
 * Created by Pika on 26/10/20.
 */

abstract class TopAdsDashStatisticFragment : TkpdBaseV4Fragment() {

    internal var summary: Summary = Summary()
    private var cells: List<Cell> = listOf()

    override fun getScreenName(): String = javaClass.simpleName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_statistics_graph_fragment, container, false)
    }

    companion object {
        @LayoutRes
        private val TOOLTIP_RES_LAYOUT = R.layout.topads_graph_tooltip
    }

    fun showLineGraph(dataStatistic: DataStatistic?) {
        if (dataStatistic == null) {
            return
        }
        this.cells = dataStatistic.cells
        with(lineGraphView) {
            init(getLineChartConfig())
            setDataSets(getLineChartData())
            invalidateChart()
        }
    }

    private fun getLineChartData(): LineChartData {
        val chartEntry: List<LineChartEntry> = cells.map {
            when {
                getIndex() == 0 -> LineChartEntry(it.impressionSum.toFloat(), it.impressionSum.toString(), getDate(it))
                getIndex() == 1 -> LineChartEntry(it.clickSum.toFloat(), it.clickSumFmt, getDate(it))
                getIndex() == 2 -> LineChartEntry(it.costSum, it.costSumFmt, getDate(it))
                getIndex() == 3 -> LineChartEntry(it.grossProfit, it.grossProfitFmt, getDate(it))
                getIndex() == 4 -> LineChartEntry(it.ctrPercentage, it.ctrPercentageFmt, getDate(it))
                getIndex() == 5 -> LineChartEntry(it.conversionSum.toFloat(), it.conversionSumFmt, getDate(it))
                getIndex() == 6 -> LineChartEntry(it.costAvg, it.costAvgFmt, getDate(it))
                else -> LineChartEntry(it.soldSum, it.soldSumFmt, getDate(it))
            }
        }


        val yAxisLabel = getYAxisLabel()

        return LineChartData(
                chartEntry = chartEntry,
                yAxisLabel = yAxisLabel,
                config = LineChartEntryConfigModel(
                        lineWidth = 1.8f
                )
        )
    }

    private fun getYAxisLabel(): List<AxisLabel> {
        return cells.map {
            when {
                getIndex() == 0 -> AxisLabel(it.impressionSum.toFloat(), it.impressionSum.toString())
                getIndex() == 1 -> AxisLabel(it.clickSum.toFloat(), it.clickSumFmt)
                getIndex() == 2 -> AxisLabel(it.costSum, it.costSumFmt)
                getIndex() == 3 -> AxisLabel(it.grossProfit, it.grossProfitFmt)
                getIndex() == 4 -> AxisLabel(it.ctrPercentage, it.ctrPercentageFmt)
                getIndex() == 5 -> AxisLabel(it.conversionSum.toFloat(), it.conversionSumFmt)
                getIndex() == 6 -> AxisLabel(it.costAvg, it.costAvgFmt)
                else -> AxisLabel(it.soldSum, it.soldSumFmt)
            }
        }
    }


    private fun getLineChartConfig(): LineChartConfigModel {
        val lineChartData = getLineChartData()

        return LineChartConfig.create {
            xAnimationDuration { 200 }
            yAnimationDuration { 200 }
            tooltipEnabled { true }
            setChartTooltip(getLineGraphTooltip())

            xAxis {
                val xAxisLabels = lineChartData.chartEntry.map { it.xLabel }
                gridEnabled { false }
                textColor { resources.getColor(R.color.Neutral_N700_96) }
                labelFormatter {
                    ChartXAxisLabelFormatter(xAxisLabels)
                }
            }
        }
    }

    private fun getLineGraphTooltip(): ChartTooltip {
        return ChartTooltip(context, TOOLTIP_RES_LAYOUT)
                .setOnDisplayContent { view, data, x, y ->
                    (data as? LineChartEntry)?.let {
                        view.tooltip_title.text = it.xLabel
                        view.tooltip_value.text = it.yLabel
                    }
                }
    }


    private fun getDate(cell: Cell): String {
        val formatterLabel = SimpleDateFormat("dd MMM")
        return formatterLabel.format(cell.date)
    }

    protected abstract fun getIndex(): Int
}
