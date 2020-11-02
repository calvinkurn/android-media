package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.charts.common.ChartColor
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
import kotlinx.android.synthetic.main.shc_partial_multi_line_chart_tooltip.view.*

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
        private val TOOLTIP_RES_LAYOUT = R.layout.shc_partial_multi_line_chart_tooltip
    }

    private val metricsAdapter by lazy { MultiLineMetricsAdapter(this) }
    private var element: MultiLineGraphWidgetUiModel? = null
    private var lastSelectedMetric: MultiLineMetricUiModel? = null
    private val metricMap: MutableMap<MultiLineMetricUiModel, LineChartData> = mutableMapOf()

    override fun bind(element: MultiLineGraphWidgetUiModel) {
        this.element = element

        val data = element.data
        when {
            data == null -> setOnLoadingState(element)
            data.error.isNotBlank() -> setOnErrorState(element)
            else -> setOnSuccessState(element)
        }
    }

    override fun onItemClickListener(metric: MultiLineMetricUiModel, position: Int) {
        itemView.rvShcGraphMetrics.post {
            itemView.rvShcGraphMetrics.layoutManager?.scrollToPosition(position)
        }

        setOnMetricStateChanged(metric)
    }

    private fun setOnMetricStateChanged(metric: MultiLineMetricUiModel) {
        val otherSelectedMetric = metricsAdapter.items.find { it.isSelected && it != metric }
        val isAnyOtherSelected = otherSelectedMetric != null
        if (this.lastSelectedMetric == metric) {
            if (isAnyOtherSelected) {
                if (lastSelectedMetric?.type == metric.type) {
                    metric.isSelected = false
                    this.lastSelectedMetric = otherSelectedMetric
                    removeMetric(metric)
                } else {
                    otherSelectedMetric?.let {
                        it.isSelected = false
                        removeMetric(it)
                    }
                    metric.isSelected = true
                    this.lastSelectedMetric = metric
                    addMetric(metric)
                }
            } else {
                metricsAdapter.notifyDataSetChanged()
                return
            }
        } else {
            if (lastSelectedMetric?.type == metric.type) {
                if (metric.isSelected) {
                    metric.isSelected = false
                    this.lastSelectedMetric = otherSelectedMetric
                    removeMetric(metric)
                } else {
                    metric.isSelected = true
                    this.lastSelectedMetric = metric
                    addMetric(metric)
                }
            } else {
                metricsAdapter.items.forEach {
                    if (it != metric) {
                        it.isSelected = false
                    }
                }
                metric.isSelected = true
                lastSelectedMetric = metric
                addMetric(metric)
            }
        }

        metricsAdapter.notifyDataSetChanged()
        initLineGraph(metric)
    }

    private fun addMetric(metric: MultiLineMetricUiModel) {
        if (metricMap[metric] == null) {
            metricMap[metric] = getLineChartData(metric)
        }
    }

    private fun removeMetric(metric: MultiLineMetricUiModel) {

    }

    private fun setOnLoadingState(element: MultiLineGraphWidgetUiModel) {

    }

    private fun setOnErrorState(element: MultiLineGraphWidgetUiModel) {

    }

    private fun setOnSuccessState(element: MultiLineGraphWidgetUiModel) {
        val metricItems = element.data?.metrics.orEmpty()
        val metric = metricItems.getOrNull(0)
        metric?.isSelected = true

        with(itemView) {
            tvShcMultiLineGraphTitle.text = element.title

            val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank()
            val ctaVisibility = if (isCtaVisible) View.VISIBLE else View.GONE
            btnShcMultiLineCta.visibility = ctaVisibility
            btnShcMultiLineCta.text = element.ctaText

            setupMetrics(metricItems)

            if (metric != null) {
                lastSelectedMetric = metric
                initLineGraph(metric)
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

    private fun initLineGraph(metric: MultiLineMetricUiModel) {
        //set metrics selected status if have same type

        with(itemView.chartViewShcMultiLine) {
            val lineChartDataSets = getLineChartData(metric)
            init(getLineGraphConfig(lineChartDataSets))
            setDataSets(lineChartDataSets)
            invalidateChart()
        }
    }

    private fun getLineGraphConfig(lineChartData: LineChartData): LineChartConfigModel {
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

    private fun getHighestYAxisValue(lineChartDataSets: List<LineChartData>): LineChartData? {
        var lineChartData: LineChartData? = lineChartDataSets.getOrNull(0)

        lineChartDataSets.forEach {
            if (lineChartData != it) {
                val maxValueCurrent = lineChartData?.yAxisLabel?.maxBy { axis -> axis.value }?.value
                        ?: 0f
                val maxValue = it.yAxisLabel.maxBy { axis -> axis.value }?.value ?: 0f

                if (maxValue >= maxValueCurrent) {
                    lineChartData = it
                }
            }
        }

        return lineChartData
    }

    private fun getLineGraphTooltip(): ChartTooltip {
        return ChartTooltip(itemView.context, TOOLTIP_RES_LAYOUT)
                .setOnDisplayContent { view, data, x, y ->
                    data?.let {
                        with(view) {
                            tvShcTooltipTitle1st.text = it.xLabel
                            tvShcTooltipValue1st.text = it.yLabel
                        }
                    }
                }
    }

    private fun getLineChartData(metric: MultiLineMetricUiModel): LineChartData {
        val hexColor = metric.summary.lineColor
        val lineHexColor = if (hexColor.isNotBlank()) {
            hexColor
        } else {
            ChartColor.DEFAULT_LINE_COLOR
        }

        val chartEntry: List<LineChartEntry> = metric.linePeriod.currentPeriod.map {
            LineChartEntry(it.yVal, it.yLabel, it.xLabel)
        }

        val yAxisLabel = getYAxisLabel(metric)

        return LineChartData(
                chartEntry = chartEntry,
                yAxisLabel = yAxisLabel,
                config = LineChartEntryConfigModel(
                        lineWidth = 1.8f,
                        drawFillEnabled = false,
                        lineColor = Color.parseColor(lineHexColor)
                )
        )
    }

    private fun getYAxisLabel(metric: MultiLineMetricUiModel): List<AxisLabel> {
        return metric.yAxis.map {
            AxisLabel(it.yValue, it.yLabel)
        }
    }

    interface Listener : BaseViewHolderListener {

    }
}