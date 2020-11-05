package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.charts.common.ChartColor
import com.tokopedia.charts.common.ChartTooltip
import com.tokopedia.charts.config.LineChartConfig
import com.tokopedia.charts.model.*
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.adapter.MultiLineMetricsAdapter
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineMetricUiModel
import com.tokopedia.sellerhomecommon.presentation.model.XYAxisUiModel
import com.tokopedia.sellerhomecommon.utils.ChartXAxisLabelFormatter
import com.tokopedia.sellerhomecommon.utils.ChartYAxisLabelFormatter
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import kotlinx.android.synthetic.main.shc_multi_line_graph_widget.view.*
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_error.view.*
import kotlinx.android.synthetic.main.shc_partial_multi_line_chart_tooltip.view.*
import kotlinx.android.synthetic.main.shc_partial_multi_line_graph_loading_state.view.*
import timber.log.Timber

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
    private var isMetricComparableByPeriodSelected: Boolean = false

    override fun bind(element: MultiLineGraphWidgetUiModel) {
        this.element = element

        val data = element.data
        when {
            data == null -> setOnLoadingState()
            data.error.isNotBlank() -> setOnErrorState(element)
            else -> setOnSuccessState(element)
        }
    }

    override fun onItemClickListener(metric: MultiLineMetricUiModel, position: Int) {
        itemView.rvShcGraphMetrics.post {
            val mPosition = if (position == 0 || metricsAdapter.itemCount.minus(1) == position) {
                position
            } else {
                position.plus(1)
            }
            itemView.rvShcGraphMetrics?.smoothScrollToPosition(mPosition)
        }

        setOnMetricStateChanged(metric)
    }

    private fun setupTooltip(element: MultiLineGraphWidgetUiModel) = with(itemView) {
        val tooltip = element.tooltip
        val shouldShowTooltip = (tooltip?.shouldShow == true) && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
        if (shouldShowTooltip) {
            tvShcMultiLineGraphTitle.setUnifyDrawableEnd(IconUnify.INFORMATION)
            tvShcMultiLineGraphTitle.setOnClickListener {
                listener.onTooltipClicked(tooltip ?: return@setOnClickListener)
            }
        } else {
            tvShcMultiLineGraphTitle.clearUnifyDrawableEnd()
        }
    }

    private fun setOnMetricStateChanged(metric: MultiLineMetricUiModel) {
        val otherSelectedMetric = metricsAdapter.items.find { it.isSelected && it != metric }
        val isAnyOtherSelected = otherSelectedMetric != null
        if (this.lastSelectedMetric == metric) {
            if (isAnyOtherSelected) {
                if (lastSelectedMetric?.type == metric.type) {
                    metric.isSelected = false
                    this.lastSelectedMetric = otherSelectedMetric
                } else {
                    otherSelectedMetric?.isSelected = false
                    metric.isSelected = true
                    this.lastSelectedMetric = metric
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
                } else {
                    metric.isSelected = true
                    this.lastSelectedMetric?.let {
                        it.isSelected = !checkIsMetricError(it)
                    }
                    this.lastSelectedMetric = metric
                }
            } else {
                metricsAdapter.items.forEach {
                    if (it != metric) {
                        it.isSelected = false
                    }
                }
                lastSelectedMetric?.isSelected = false
                metric.isSelected = true
                lastSelectedMetric = metric
            }
        }

        if (checkIsMetricError(metric)) {
            metricsAdapter.items.forEach {
                if (it != metric) {
                    it.isSelected = false
                }
            }
        }

        metricsAdapter.notifyDataSetChanged()
        val selectedMetrics = metricsAdapter.items.filter { it.isSelected }
        showLineGraph(selectedMetrics)
    }

    private fun setOnLoadingState() {
        with(itemView) {
            shcMlgSuccessState.gone()
            commonWidgetErrorState.gone()
            shcMlgLoadingState.visible()
        }
    }

    private fun setOnErrorState(element: MultiLineGraphWidgetUiModel) {
        with(itemView) {
            tvShcMultiLineGraphTitle.text = element.title
            shcMlgLoadingState.gone()
            shcMlgSuccessState.visible()
            getWidgetComponents().forEach {
                it.gone()
            }
            commonWidgetErrorState.visible()
            ImageHandler.loadImageWithId(imgWidgetOnError, R.drawable.unify_globalerrors_connection)
        }

        setupTooltip(element)
    }

    private fun setOnSuccessState(element: MultiLineGraphWidgetUiModel) {
        val metricItems = element.data?.metrics.orEmpty()

        if (metricItems.isEmpty()) {
            setOnErrorState(element)
            return
        }

        val metric = metricItems.getOrNull(0)
        metric?.isSelected = true

        with(itemView) {
            shcMlgLoadingState.gone()
            commonWidgetErrorState.gone()
            shcMlgSuccessState.visible()
            getWidgetComponents().forEach {
                it.visible()
            }

            tvShcMultiLineGraphTitle.text = element.title

            setupMetricCards(metricItems)
            hideLegendView()

            if (metric != null) {
                lastSelectedMetric = metric
                showLineGraph(listOf(metric))
            }

            setupCta(element)
            setupTooltip(element)
        }
    }

    private fun getWidgetComponents(): List<View> {
        return with(itemView) {
            listOf(rvShcGraphMetrics, lvShcCurrentPeriod, lvShcLastPeriod,
                    chartViewShcMultiLine, imgShcMultiLineCta, tvShcMultiLineCta)
        }
    }

    private fun setupCta(element: MultiLineGraphWidgetUiModel) = with(itemView) {
        val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank()
        val ctaVisibility = if (isCtaVisible) View.VISIBLE else View.GONE
        tvShcMultiLineCta.visibility = ctaVisibility
        imgShcMultiLineCta.visibility = ctaVisibility
        if (isCtaVisible) {
            tvShcMultiLineCta.text = element.ctaText
            tvShcMultiLineCta.setOnClickListener {
                openAppLink(element.appLink)
            }
            imgShcMultiLineCta.setOnClickListener {
                openAppLink(element.appLink)
            }
        }
    }

    private fun openAppLink(appLink: String): Boolean = RouteManager.route(itemView.context, appLink)

    private fun showLegendView() {
        with(itemView) {
            lvShcCurrentPeriod.setText(context.getString(R.string.shc_current_period))
            lvShcCurrentPeriod.visibility = View.VISIBLE
            lvShcCurrentPeriod.showLine()
            lvShcLastPeriod.setText(context.getString(R.string.shc_last_period))
            lvShcLastPeriod.visibility = View.VISIBLE
            lvShcLastPeriod.showDashLine()
        }
    }

    private fun hideLegendView() {
        with(itemView) {
            lvShcCurrentPeriod.visibility = View.INVISIBLE
            lvShcLastPeriod.visibility = View.INVISIBLE
        }
    }

    private fun setupMetricCards(items: List<MultiLineMetricUiModel>) {
        with(itemView) {
            rvShcGraphMetrics.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvShcGraphMetrics.adapter = metricsAdapter

            metricsAdapter.setItems(items)
            rvShcGraphMetrics.post {
                metricsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun showLineGraph(metrics: List<MultiLineMetricUiModel>) {
        with(itemView.chartViewShcMultiLine) {
            val lineChartDataSets = getLineChartData(metrics)
            init(getLineGraphConfig(lineChartDataSets))
            setDataSets(*lineChartDataSets.toTypedArray())
            invalidateChart()
        }
    }

    private fun getLineGraphConfig(lineChartDataSets: List<LineChartData>): LineChartConfigModel {

        val lineChartData: LineChartData? = getHighestYAxisValue(lineChartDataSets)

        return LineChartConfig.create {
            xAnimationDuration { 200 }
            yAnimationDuration { 200 }
            tooltipEnabled { true }
            setChartTooltip(getLineGraphTooltip())

            xAxis {
                val xAxisLabels = lineChartData?.chartEntry?.map { it.xLabel }.orEmpty()
                gridEnabled { false }
                textColor { itemView.context.getResColor(R.color.Neutral_N700_96) }
                labelFormatter {
                    ChartXAxisLabelFormatter(xAxisLabels)
                }
            }

            yAxis {
                val yAxisLabels = lineChartData?.yAxisLabel.orEmpty()
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
                        if (isMetricComparableByPeriodSelected) {
                            showComparablePeriodMetricTooltip(view, it, x.toInt())
                        } else {
                            showComparedMetricsTooltip(view, it, x.toInt())
                        }
                    }
                }
    }

    private fun showComparedMetricsTooltip(view: View, entry: LineChartEntry, axisIndex: Int) {
        val selectedMetrics = metricsAdapter.items.filter { it.isSelected }
        with(view) {
            ttvShcMlgTooltip1.showDot(Color.TRANSPARENT)
            ttvShcMlgTooltip1.setContent(entry.xLabel, entry.yLabel)
            tvShcTooltipHeader.visibility = View.VISIBLE
            tvShcTooltipHeader.text = entry.xLabel

            //show first metric tooltip
            try {
                selectedMetrics[0].let { metric ->
                    val hexColor = getLineHexColor(metric.summary.lineColor)
                    val value = getTooltipValue(metric, axisIndex)
                    ttvShcMlgTooltip1.setContent(metric.summary.title, value)
                    ttvShcMlgTooltip1.showDot(Color.parseColor(hexColor))
                }
            } catch (e: IndexOutOfBoundsException) {
                Timber.i(e)
            }

            //show second metric tooltip
            try {
                selectedMetrics[1].let { metric ->
                    val hexColor = getLineHexColor(metric.summary.lineColor)
                    val value = getTooltipValue(metric, axisIndex)
                    ttvShcMlgTooltip2.visibility = View.VISIBLE
                    ttvShcMlgTooltip2.setContent(metric.summary.title, value)
                    ttvShcMlgTooltip2.showDot(Color.parseColor(hexColor))
                }
            } catch (e: IndexOutOfBoundsException) {
                ttvShcMlgTooltip2.visibility = View.GONE
                Timber.i(e)
            }
        }
    }

    private fun showComparablePeriodMetricTooltip(view: View, entry: LineChartEntry, axisIndex: Int) {
        with(view) {
            lastSelectedMetric?.let { metric ->
                val hexColor = getLineHexColor(metric.summary.lineColor)
                ttvShcMlgTooltip1.showDot(Color.parseColor(hexColor))
                ttvShcMlgTooltip1.setContent(entry.xLabel, entry.yLabel)

                //show current period tooltip
                try {
                    metric.linePeriod.currentPeriod[axisIndex].let {
                        ttvShcMlgTooltip1.showDot(Color.parseColor(hexColor))
                        ttvShcMlgTooltip1.setContent(it.xLabel, it.yLabel)
                    }
                } catch (e: IndexOutOfBoundsException) {
                    Timber.i(e)
                }

                //show last period tooltip
                try {
                    metric.linePeriod.lastPeriod[axisIndex].let {
                        ttvShcMlgTooltip2.visibility = View.VISIBLE
                        ttvShcMlgTooltip2.showDot(Color.parseColor(hexColor), true)
                        ttvShcMlgTooltip2.setContent(it.xLabel, it.yLabel)
                    }
                } catch (e: IndexOutOfBoundsException) {
                    ttvShcMlgTooltip2.visibility = View.GONE
                    Timber.i(e)
                }
            }
        }
    }

    private fun getTooltipValue(metric: MultiLineMetricUiModel, axisIndex: Int): String {
        return try {
            metric.linePeriod.currentPeriod[axisIndex].yLabel
        } catch (e: IndexOutOfBoundsException) {
            ""
        }
    }

    /**
     * mapping the given single/multiple metrics to become list of LineChartData.
     * return the list of LineChartData from `current` and `lastPeriod` if given single metric
     * and the lastPeriod is available. Else, will return the list of LineChartData
     * from current period of each metric.
     * */
    private fun getLineChartData(metrics: List<MultiLineMetricUiModel>): List<LineChartData> {
        val isSingleMetric = metrics.size == 1

        //map the `current` and `lastPeriod`
        if (isSingleMetric) {
            val metric = metrics[0]

            if (checkIsMetricError(metric)) {
                showMetricErrorState()
                return emptyList()
            }

            val isComparableByPeriod = metricsAdapter.items.filter { it.type == metric.type }.size == 1
            isMetricComparableByPeriodSelected = isComparableByPeriod
            if (isComparableByPeriod && metric.linePeriod.lastPeriod.isNotEmpty()) {
                showLegendView()
                return getLineChartDataByPeriod(metric)
            }
        } else {
            isMetricComparableByPeriodSelected = false
        }

        hideLegendView()

        val successStateMetrics = metrics.filter { !checkIsMetricError(it) }
        if (successStateMetrics.isEmpty()) {
            showMetricErrorState()
            return emptyList()
        }

        clearMetricErrorState()

        return successStateMetrics.map { metric ->
            val hexColor = getLineHexColor(metric.summary.lineColor)
            val chartEntry: List<LineChartEntry> = getLineChartEntry(metric.linePeriod.currentPeriod)
            val yAxisLabel = getYAxisLabel(metric)

            return@map LineChartData(
                    chartEntry = chartEntry,
                    yAxisLabel = yAxisLabel,
                    config = LineChartEntryConfigModel(
                            lineWidth = 1.8f,
                            drawFillEnabled = false,
                            lineColor = Color.parseColor(hexColor)
                    )
            )
        }
    }

    private fun showMetricErrorState() {
        with(itemView) {
            chartViewShcMultiLine.gone()
            commonWidgetErrorState.visible()
            ImageHandler.loadImageWithId(imgWidgetOnError, R.drawable.unify_globalerrors_connection)
        }
    }

    private fun clearMetricErrorState() {
        with(itemView) {
            chartViewShcMultiLine.visible()
            commonWidgetErrorState.gone()
        }
    }

    private fun checkIsMetricError(metric: MultiLineMetricUiModel): Boolean {
        return metric.errorMsg.isNotEmpty() || metric.isError ||
                metric.linePeriod.currentPeriod.isEmpty()
    }

    private fun getLineChartDataByPeriod(metric: MultiLineMetricUiModel): List<LineChartData> {
        val currentPeriod: List<LineChartEntry> = getLineChartEntry(metric.linePeriod.currentPeriod)
        val lastPeriod: List<LineChartEntry> = getLineChartEntry(metric.linePeriod.lastPeriod)
        val hexColor = getLineHexColor(metric.summary.lineColor)
        val yAxisLabel = getYAxisLabel(metric)

        return listOf(lastPeriod, currentPeriod).map {
            val isLastPeriod = it == lastPeriod
            return@map LineChartData(
                    chartEntry = it,
                    yAxisLabel = yAxisLabel,
                    config = LineChartEntryConfigModel(
                            lineWidth = if (isLastPeriod) 1.2f else 1.8f,
                            drawFillEnabled = false,
                            lineColor = Color.parseColor(hexColor),
                            isLineDashed = isLastPeriod
                    )
            )
        }
    }

    private fun getLineChartEntry(periodAxis: List<XYAxisUiModel>): List<LineChartEntry> {
        return periodAxis.map {
            LineChartEntry(it.yVal, it.yLabel, it.xLabel)
        }
    }

    private fun getLineHexColor(hexColor: String): String {
        return if (hexColor.isNotBlank()) {
            hexColor
        } else {
            ChartColor.DEFAULT_LINE_COLOR
        }
    }

    private fun getYAxisLabel(metric: MultiLineMetricUiModel): List<AxisLabel> {
        return metric.yAxis.map {
            AxisLabel(it.yValue, it.yLabel)
        }
    }

    interface Listener : BaseViewHolderListener {

    }
}