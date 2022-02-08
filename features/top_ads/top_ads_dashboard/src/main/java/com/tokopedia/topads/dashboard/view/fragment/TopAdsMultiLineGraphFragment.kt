package com.tokopedia.topads.dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.charts.config.LineChartConfig
import com.tokopedia.charts.model.*
import com.tokopedia.charts.view.LineChartView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.beranda.TopadsWidgetSummaryStatisticsModel
import com.tokopedia.topads.dashboard.data.utils.ChartXAxisLabelFormatter
import kotlinx.android.synthetic.main.topads_statistics_graph_fragment.*
import java.text.SimpleDateFormat

class TopAdsMultiLineGraphFragment : TkpdBaseV4Fragment() {

    private var cells: List<TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics.Cell> =
        listOf()

    override fun getScreenName(): String = javaClass.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.topads_statistics_graph_fragment, container, false)
    }

    private data class StatsData(val data: Float, val dataStr: String, val date: String)

    fun setValue(cells: List<TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics.Cell>) {
        this.cells = cells
    }

    fun showLineGraph(items: List<MultiLineGraph>) {
        with(lineGraphView) {
            init(getLineChartConfig())
            setDataForMultipleLine(items)
            invalidateChart()
        }
    }

    private fun setDataForMultipleLine(items: List<MultiLineGraph>) {
        val dataSets = items.map { getDataForSingleLine(it) }
        lineGraphView?.setDataSets(*dataSets.toTypedArray())
    }

    private fun getDataForSingleLine(item: MultiLineGraph): LineChartData {
        val chartEntry: List<LineChartEntry> = getCellData(item.id).map {
            LineChartEntry(it.data, it.dataStr, it.date)
        }

        val yAxisLabel = getYAxisLabel(item.id)

        return LineChartData(
            chartEntry = chartEntry,
            yAxisLabel = yAxisLabel,
            config = LineChartEntryConfigModel(
                lineWidth = 1.8f,
                lineColor = item.color,
                fillColor = ContextCompat.getColor(
                    requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_B100_44
                )
            )
        )
    }

    private fun getCellData(id: Int): List<StatsData> {
        return cells.map {
            when (id) {
                TopAdsDashboardConstant.CONST_TAMPIL ->
                    StatsData(it.impression.toFloat(), it.impressionFmt, getDate(it))
                TopAdsDashboardConstant.CONST_KLIK ->
                    StatsData(it.click.toFloat(), it.clickFmt, getDate(it))
                TopAdsDashboardConstant.CONST_TERJUAL ->
                    StatsData(it.sold.toFloat(), it.soldFmt, getDate(it))
                TopAdsDashboardConstant.CONST_PENDAPATAN ->
                    StatsData(it.income.toFloat(), it.incomeFmt, getDate(it))
                TopAdsDashboardConstant.CONST_PENGELURAN ->
                    StatsData(it.cost.toFloat(), it.costFmt, getDate(it))
                TopAdsDashboardConstant.CONST_EFECTIVITAS_IKLAN ->
                    StatsData(it.roas.toFloat(), it.roasFmt, getDate(it))
                else -> StatsData(0f, "0", "0")
            }
        }
    }

    private fun getYAxisLabel(id: Int): List<AxisLabel> {
        return getCellData(id).map {
            AxisLabel(it.data, it.dataStr)
        }
    }

    private fun getLineChartConfig(): LineChartConfigModel {
        val lineChartData = getDataForSingleLine(
            MultiLineGraph(
                0, ContextCompat.getColor(
                    requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_B100_44
                )
            )
        )

        return LineChartConfig.create {
            xAnimationDuration { 200 }
            yAnimationDuration { 200 }
            tooltipEnabled { true }
            chartLineMode { LineChartView.LINE_MODE_CURVE }

            xAxis {
                val xAxisLabels = lineChartData.chartEntry.map { it.xLabel }
                gridEnabled { false }
                context?.let {
                    textColor {
                        ContextCompat.getColor(
                            it, (com.tokopedia.unifyprinciples.R.color.Neutral_N700_96)
                        )
                    }
                }
                labelFormatter {
                    ChartXAxisLabelFormatter(xAxisLabels)
                }
            }
        }
    }

    private fun getDate(cell: TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics.Cell): String {
        val formater = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = formater.parse(cell.date)
        return SimpleDateFormat("dd MMM").format(date)
    }

    data class MultiLineGraph(val id: Int, val color: Int)
}