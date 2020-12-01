package com.tokopedia.topads.dashboard.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.charts.common.ChartTooltip
import com.tokopedia.charts.config.LineChartConfig
import com.tokopedia.charts.model.*
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_0
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_1
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_2
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_3
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_4
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_5
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_6
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

    companion object {
        @LayoutRes
        private val TOOLTIP_RES_LAYOUT = R.layout.topads_graph_tooltip
    }

    internal var summary: Summary = Summary()
    private var cells: List<Cell> = listOf()

    override fun getScreenName(): String = javaClass.simpleName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_statistics_graph_fragment, container, false)
    }

    private data class StatsData(val data: Float, val dataStr: String, val date: String)


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

    private fun getCellData(): List<StatsData> {
        return cells.map {
            when {
                getIndex() == CONST_0 -> StatsData(it.impressionSum.toFloat(), it.impressionSum.toString(), getDate(it))
                getIndex() == CONST_1 -> StatsData(it.clickSum.toFloat(), it.clickSumFmt, getDate(it))
                getIndex() == CONST_2 -> StatsData(it.costSum, it.costSumFmt, getDate(it))
                getIndex() == CONST_3 -> StatsData(it.grossProfit, it.grossProfitFmt, getDate(it))
                getIndex() == CONST_4 -> StatsData(it.ctrPercentage, it.ctrPercentageFmt, getDate(it))
                getIndex() == CONST_5 -> StatsData(it.conversionSum.toFloat(), it.conversionSumFmt, getDate(it))
                getIndex() == CONST_6 -> StatsData(it.costAvg, it.costAvgFmt, getDate(it))
                else -> StatsData(it.soldSum, it.soldSumFmt, getDate(it))
            }
        }
    }

    private fun getLineChartData(): LineChartData {

        val chartEntry: List<LineChartEntry> = getCellData().map {
            LineChartEntry(it.data, it.dataStr, it.date)
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
        return getCellData().map {
            AxisLabel(it.data, it.dataStr)
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
                context?.let {
                    textColor { ContextCompat.getColor(it, (com.tokopedia.unifyprinciples.R.color.Neutral_N700_96)) }
                }
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

    //to get the position of fragment
    protected abstract fun getIndex(): Int

}
