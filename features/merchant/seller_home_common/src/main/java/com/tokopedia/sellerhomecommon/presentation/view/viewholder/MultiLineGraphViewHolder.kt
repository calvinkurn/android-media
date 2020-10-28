package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.charts.common.ChartTooltip
import com.tokopedia.charts.config.LineChartConfig
import com.tokopedia.charts.model.*
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.adapter.MultiLineMetricsAdapter
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineMetricUiModel
import com.tokopedia.sellerhomecommon.utils.ChartXAxisLabelFormatter
import com.tokopedia.sellerhomecommon.utils.ChartYAxisLabelFormatter
import kotlinx.android.synthetic.main.shc_multi_line_graph_widget.view.*
import kotlinx.android.synthetic.main.shc_partial_chart_tooltip.view.*

/**
 * Created By @ilhamsuaib on 27/10/20
 */

class MultiLineGraphViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<MultiLineGraphWidgetUiModel>(itemView), MultiLineMetricsAdapter.MetricsListener {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_multi_line_graph_widget

        @LayoutRes
        private val TOOLTIP_RES_LAYOUT = R.layout.shc_partial_chart_tooltip
    }

    private val metricsAdapter by lazy { MultiLineMetricsAdapter(this) }
    private var lastSelectedMetric: MultiLineMetricUiModel? = null

    override fun bind(element: MultiLineGraphWidgetUiModel) {
        val data = element.data
        when {
            data == null -> setOnLoadingState(element)
            data.error.isNotBlank() -> setOnErrorState(element)
            else -> setOnSuccessState(element)
        }
    }

    override fun onItemClickListener(metric: MultiLineMetricUiModel, position: Int) {
        if (lastSelectedMetric == metric) {
            return
        }

        lastSelectedMetric = metric
        itemView.rvShcGraphMetrics.scrollToPosition(position)
        showLineGraph(metric)
    }

    private fun setOnLoadingState(element: MultiLineGraphWidgetUiModel) {

    }

    private fun setOnErrorState(element: MultiLineGraphWidgetUiModel) {

    }

    private fun setOnSuccessState(element: MultiLineGraphWidgetUiModel) {
        val metric = element.data?.metrics?.getOrNull(0)
        lastSelectedMetric = metric

        with(itemView) {
            tvShcMultiLineGraphTitle.text = element.title

            val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank() && isShown
            val ctaVisibility = if (isCtaVisible) View.VISIBLE else View.GONE
            btnShcMultiLineCta.visibility = ctaVisibility

            setupMetrics(element.data?.metrics.orEmpty())

            if (metric != null) {
                showLineGraph(metric)
            }
        }
    }

    private fun setupMetrics(items: List<MultiLineMetricUiModel>) {
        with(itemView) {
            rvShcGraphMetrics.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvShcGraphMetrics.adapter = metricsAdapter

            metricsAdapter.setItems(items)
            rvShcGraphMetrics.post {
                metricsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun showLineGraph(metric: MultiLineMetricUiModel) {
        with(itemView.chartViewShcMultiLine) {
            init(getLineGraphConfig(metric))
            setDataSets(getLineChartData(metric))
            invalidateChart()
        }
    }

    private fun getLineGraphConfig(element: MultiLineMetricUiModel): LineChartConfigModel {
        val lineChartData = getLineChartData(element)
        return LineChartConfig.create {
            xAnimationDuration { 200 }
            yAnimationDuration { 200 }
            tooltipEnabled { true }
            setChartTooltip(getLineGraphTooltip())

            xAxis {
                val xAxisLabels = lineChartData.chartEntry.map { it.xLabel }
                gridEnabled { false }
                textColor { itemView.context.getResColor(R.color.Neutral_N700_96) }
                labelFormatter {
                    ChartXAxisLabelFormatter(xAxisLabels)
                }
            }

            yAxis {
                val yAxisLabels = lineChartData.yAxisLabel
                textColor { itemView.context.getResColor(R.color.Neutral_N700_96) }
                labelFormatter {
                    ChartYAxisLabelFormatter(yAxisLabels)
                }
                labelCount { yAxisLabels.size }
            }
        }
    }

    private fun getLineGraphTooltip(): ChartTooltip {
        return ChartTooltip(itemView.context, TOOLTIP_RES_LAYOUT)
                .setOnDisplayContent { view, data, x, y ->
                    (data as? LineChartEntry)?.let {
                        view.tvShcTooltipTitle.text = it.xLabel
                        view.tvShcTooltipValue.text = it.yLabel
                    }
                }
    }

    private fun getLineChartData(element: MultiLineMetricUiModel): LineChartData {

        val chartEntry: List<LineChartEntry> = element.linePeriod.currentPeriod.map {
            LineChartEntry(it.yVal, it.yLabel, it.xLabel)
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
        return lastSelectedMetric?.yAxis?.map {
            AxisLabel(it.yValue, it.yLabel)
        }.orEmpty()
    }

    interface Listener : BaseViewHolderListener {

    }
}