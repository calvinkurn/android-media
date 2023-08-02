package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.charts.common.ChartColor
import com.tokopedia.charts.common.ChartTooltip
import com.tokopedia.charts.config.LineChartConfig
import com.tokopedia.charts.model.AxisLabel
import com.tokopedia.charts.model.LineChartConfigModel
import com.tokopedia.charts.model.LineChartData
import com.tokopedia.charts.model.LineChartEntry
import com.tokopedia.charts.model.LineChartEntryConfigModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcMultiLineGraphWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.MultiLineMetricsAdapter
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineMetricUiModel
import com.tokopedia.sellerhomecommon.presentation.model.XYAxisUiModel
import com.tokopedia.sellerhomecommon.presentation.view.customview.MultiLineGraphTooltipView
import com.tokopedia.sellerhomecommon.utils.ChartXAxisLabelFormatter
import com.tokopedia.sellerhomecommon.utils.ChartYAxisLabelFormatter
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifyprinciples.Typography
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 27/10/20
 */

class MultiLineGraphViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<MultiLineGraphWidgetUiModel>(itemView),
    MultiLineMetricsAdapter.MetricsListener {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_multi_line_graph_widget

        @LayoutRes
        private val TOOLTIP_RES_LAYOUT = R.layout.shc_partial_multi_line_chart_tooltip

        private const val ANIMATION_DURATION = 200
        private const val LINE_WIDTH_NORMAL = 1.2f
        private const val LINE_WIDTH_BOLD = 1.8f
        private const val ANIMATION_START = 0f
        private const val ANIMATION_END = 1f
    }

    private val binding by lazy { ShcMultiLineGraphWidgetBinding.bind(itemView) }
    private val emptyStateBinding by lazy { binding.shcMultiLineGraphEmptyStateView }
    private val loadingStateBinding by lazy { binding.shcMultiLineGraphLoadingView }

    private val metricsAdapter by lazy { MultiLineMetricsAdapter(this) }
    private var element: MultiLineGraphWidgetUiModel? = null
    private var lastSelectedMetric: MultiLineMetricUiModel? = null
    private var isMetricComparableByPeriodSelected: Boolean = false
    private var showAnimation: ValueAnimator? = null
    private var hideAnimation: ValueAnimator? = null
    private var shouldShowEmptyState: Boolean = false

    override fun bind(element: MultiLineGraphWidgetUiModel) {
        showAnimation?.end()
        hideAnimation?.end()
        this.element = element

        val data = element.data
        when {
            data == null || element.showLoadingState -> setOnLoadingState()
            data.error.isNotBlank() -> setOnErrorState(element)
            else -> setOnSuccessState(element)
        }
    }

    override fun onItemClickListener(metric: MultiLineMetricUiModel, position: Int) {
        scrollMetricToPosition(position)
        setOnMetricStateChanged(metric)
        element?.let {
            listener.sendMultiLineGraphMetricClick(it, metric)
        }
    }

    private fun scrollMetricToPosition(position: Int) {
        binding.rvShcGraphMetrics.post {
            val mPosition = if (position == Int.ZERO ||
                metricsAdapter.itemCount.minus(Int.ONE) == position
            ) {
                position
            } else {
                position.plus(Int.ONE)
            }

            try {
                binding.rvShcGraphMetrics.smoothScrollToPosition(mPosition)
            } catch (e: IllegalArgumentException) {
                Timber.e(e)
            }
        }
    }

    private fun setTagNotification(tag: String) {
        val isTagVisible = tag.isNotBlank()
        with(binding) {
            notifTagMultiLineGraph.showWithCondition(isTagVisible)
            if (isTagVisible) {
                notifTagMultiLineGraph.setNotification(
                    tag,
                    NotificationUnify.TEXT_TYPE,
                    NotificationUnify.COLOR_TEXT_TYPE
                )
            }
        }
    }

    private fun setupTooltip(element: MultiLineGraphWidgetUiModel) = with(binding) {
        val tooltip = element.tooltip
        val shouldShowTooltip =
            (tooltip?.shouldShow == true) && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
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
        if (element?.isComparePeriodOnly == true) {
            metricsAdapter.items.forEach {
                it.isSelected = (it == metric)
            }
            this.lastSelectedMetric = metric
        } else {
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
                    metric.isSelected = true
                }
            } else {
                if (lastSelectedMetric?.type == metric.type) {
                    if (metric.isSelected) {
                        metric.isSelected = false
                        this.lastSelectedMetric = otherSelectedMetric
                    } else {
                        metric.isSelected = true
                        this.lastSelectedMetric?.let {
                            it.isSelected = !it.isError
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
        }

        if (metric.isError) {
            metricsAdapter.items.forEach {
                if (it != metric) {
                    it.isSelected = false
                }
            }
        }

        notifyAdapterDateChanged()
        val selectedMetrics = metricsAdapter.items.filter { it.isSelected }
        showLineGraph(selectedMetrics)
    }

    private fun setOnLoadingState() {
        binding.shcMlgSuccessState.gone()
        binding.luvShcMultiLineGraph.setRefreshButtonVisibility(false)
        binding.shcMultiLineGraphErrorView.gone()
        binding.shcMultiLineGraphEmptyStateView.multiLineEmptyState.gone()
        loadingStateBinding.shcMlgLoadingState.visible()
    }

    private fun setOnErrorState(element: MultiLineGraphWidgetUiModel) {
        setupTitle(element.title)
        loadingStateBinding.shcMlgLoadingState.gone()
        binding.shcMultiLineGraphEmptyStateView.multiLineEmptyState.gone()
        binding.shcMlgSuccessState.visible()
        binding.luvShcMultiLineGraph.setRefreshButtonVisibility(false)
        getWidgetComponents().forEach {
            it.gone()
        }
        binding.shcMultiLineGraphErrorView.visible()
        binding.shcMultiLineGraphErrorView.setOnReloadClicked {
            listener.onReloadWidget(element)
        }

        setTagNotification(element.tag)
        setupTooltip(element)
    }

    private fun setupTitle(title: String) {
        with(binding) {
            if (title.isNotBlank()) {
                tvShcMultiLineGraphTitle.visible()
                tvShcMultiLineGraphTitle.text = title

                val dimen12dp = root.context.resources.getDimension(R.dimen.shc_dimen_12dp).toInt()
                val dimen8dp =
                    root.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)
                        .toInt()
                rvShcGraphMetrics.setMargin(dimen12dp, dimen8dp, dimen12dp, Int.ZERO)
            } else {
                val dimen12dp = root.context.resources.getDimension(R.dimen.shc_dimen_12dp).toInt()
                val dimen16dp =
                    root.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
                        .toInt()
                tvShcMultiLineGraphTitle.gone()
                rvShcGraphMetrics.setMargin(dimen12dp, dimen16dp, dimen12dp, Int.ZERO)
            }
        }
    }

    private fun setOnSuccessState(element: MultiLineGraphWidgetUiModel) {
        val metricItems = element.data?.metrics.orEmpty()

        val metric = if (metricItems.contains(lastSelectedMetric)) {
            lastSelectedMetric
        } else {
            metricItems.getOrNull(Int.ZERO)
        }
        metric?.isSelected = true

        with(binding) {
            loadingStateBinding.shcMlgLoadingState.gone()
            shcMultiLineGraphErrorView.gone()
            shcMlgSuccessState.visible()
            setupTitle(element.title)

            getWidgetComponents().forEach {
                it.visible()
            }

            setupMetricCards(metricItems)
            hideLegendView()

            if (metric != null) {
                lastSelectedMetric = metric
                showLineGraph(listOf(metric))
            }

            val metricPosition = metricItems.indexOf(metric)
            scrollMetricToPosition(metricPosition)

            setupLastUpdated(element)
            setupCta(element)
            setTagNotification(element.tag)
            setupTooltip(element)

            horLineShcMultiLineGraphBtm.isVisible = luvShcMultiLineGraph.isVisible ||
                tvShcMultiLineCta.isVisible
            root.addOnImpressionListener(element.impressHolder) {
                listener.sendMultiLineGraphImpressionEvent(element)
            }
        }
    }

    private fun setupLastUpdated(element: MultiLineGraphWidgetUiModel) {
        element.data?.lastUpdated?.let { lastUpdated ->
            val shouldShowRefreshBtn = element.data?.lastUpdated?.needToUpdated.orFalse()

            binding.luvShcMultiLineGraph.run {
                isVisible = lastUpdated.isEnabled
                setLastUpdated(lastUpdated.lastUpdatedInMillis)
                setRefreshButtonVisibility(shouldShowRefreshBtn)
                setRefreshButtonClickListener {
                    reloadWidget(element)
                }
            }
        }
    }

    private fun reloadWidget(element: MultiLineGraphWidgetUiModel) {
        listener.onReloadWidget(element)
    }

    private fun setupEmptyState() {
        element?.let { element ->
            with(binding.shcMultiLineGraphEmptyStateView) {
                val emptyState = element.emptyState
                multiLineEmptyState.visible()
                tvLineGraphEmptyStateTitle.text = emptyState.title
                tvLineGraphEmptyStateDescription.text = emptyState.description
                tvShcMultiLineEmptyStateCta.text = emptyState.ctaText
                tvShcMultiLineEmptyStateCta.setOnClickListener {
                    if (RouteManager.route(itemView.context, emptyState.appLink)) {
                        listener.sendMultiLineGraphEmptyStateCtaClick(element)
                    }
                }
                animateShowEmptyState()
            }
        }
    }

    private fun getWidgetComponents(): List<View> {
        return with(binding) {
            listOf(
                rvShcGraphMetrics,
                lvShcCurrentPeriod,
                lvShcLastPeriod,
                chartViewShcMultiLine,
                tvShcMultiLineCta
            )
        }
    }

    private fun setupCta(element: MultiLineGraphWidgetUiModel) = with(binding) {
        val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank()
        val ctaVisibility = if (isCtaVisible) View.VISIBLE else View.GONE
        tvShcMultiLineCta.visibility = ctaVisibility
        if (isCtaVisible) {
            tvShcMultiLineCta.text = element.ctaText
            tvShcMultiLineCta.setOnClickListener {
                openAppLink(element)
            }

            val iconColor = root.context.getResColor(
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
            val iconWidth = root.context.resources.getDimension(
                com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
            )
            val iconHeight = root.context.resources.getDimension(
                com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
            )
            tvShcMultiLineCta.setUnifyDrawableEnd(
                IconUnify.CHEVRON_RIGHT,
                iconColor,
                iconWidth,
                iconHeight
            )
        }
    }

    private fun openAppLink(element: MultiLineGraphWidgetUiModel) {
        val isRouted = RouteManager.route(itemView.context, element.appLink)
        if (isRouted) {
            listener.sendMultiLineGraphCtaClick(element)
        }
    }

    private fun showLegendView() {
        with(binding) {
            lvShcCurrentPeriod.setText(root.context.getString(R.string.shc_current_period))
            lvShcCurrentPeriod.visible()
            lvShcCurrentPeriod.showLine()
            lvShcLastPeriod.setText(root.context.getString(R.string.shc_last_period))
            lvShcLastPeriod.visible()
            lvShcLastPeriod.showDashLine()
        }
    }

    private fun hideLegendView() {
        with(binding) {
            lvShcCurrentPeriod.invisible()
            lvShcLastPeriod.invisible()
        }
    }

    private fun setupMetricCards(items: List<MultiLineMetricUiModel>) {
        with(binding) {
            shcMlgSuccessState.post {
                metricsAdapter.setRecyclerViewWidth(shcMlgSuccessState.width)
            }

            if (rvShcGraphMetrics.layoutManager == null) {
                rvShcGraphMetrics.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
                rvShcGraphMetrics.adapter = metricsAdapter
            }

            metricsAdapter.setItems(items)
            rvShcGraphMetrics.post {
                notifyAdapterDateChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyAdapterDateChanged() {
        metricsAdapter.notifyDataSetChanged()
    }

    private fun showLineGraph(metrics: List<MultiLineMetricUiModel>) {
        shouldShowEmptyState = showEmpty(element, metrics)
        with(binding.chartViewShcMultiLine) {
            val lineChartDataSets = getLineChartData(metrics)
            init(getLineGraphConfig(lineChartDataSets))
            setDataSets(*lineChartDataSets.toTypedArray())
            invalidateChart()
        }

        if (shouldShowEmptyState) {
            setupEmptyState()
        } else {
            animateHideEmptyState()
        }
    }

    private fun showEmpty(
        element: MultiLineGraphWidgetUiModel?,
        metrics: List<MultiLineMetricUiModel>
    ): Boolean {
        return element != null && element.isShowEmpty && metrics.filter { it.isSelected }
            .all { it.isEmpty } &&
            element.emptyState.title.isNotBlank() && element.emptyState.description.isNotBlank() &&
            element.emptyState.ctaText.isNotBlank() && element.emptyState.appLink.isNotBlank()
    }

    private fun getLineGraphConfig(lineChartDataSets: List<LineChartData>): LineChartConfigModel {
        val lineChartData: LineChartData? = getHighestYAxisValue(lineChartDataSets)

        return LineChartConfig.create {
            xAnimationDuration { ANIMATION_DURATION }
            yAnimationDuration { ANIMATION_DURATION }
            tooltipEnabled { !shouldShowEmptyState }
            setChartTooltip(getLineGraphTooltip())

            xAxis {
                val xAxisLabels = lineChartData?.chartEntry?.map { it.xLabel }.orEmpty()
                gridEnabled { false }
                textColor { itemView.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600) }
                labelFormatter {
                    ChartXAxisLabelFormatter(xAxisLabels)
                }
            }

            yAxis {
                val yAxisLabels = lineChartData?.yAxisLabel.orEmpty()
                textColor { itemView.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600) }
                labelFormatter {
                    ChartYAxisLabelFormatter(yAxisLabels)
                }
                labelCount { yAxisLabels.size }
            }
        }
    }

    private fun getHighestYAxisValue(lineChartDataSets: List<LineChartData>): LineChartData? {
        var lineChartData: LineChartData? = lineChartDataSets.getOrNull(Int.ZERO)

        lineChartDataSets.forEach {
            if (lineChartData != it) {
                val maxValueCurrent =
                    lineChartData?.yAxisLabel?.maxByOrNull { axis -> axis.value }?.value.orZero()
                val maxValue = it.yAxisLabel.maxByOrNull { axis -> axis.value }?.value.orZero()

                if (maxValue >= maxValueCurrent) {
                    lineChartData = it
                }
            }
        }

        return lineChartData
    }

    private fun getLineGraphTooltip(): ChartTooltip {
        return ChartTooltip(itemView.context, TOOLTIP_RES_LAYOUT)
            .setOnDisplayContent { view, data, x, _ ->
                (data as? LineChartEntry)?.let {
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
            val lineGraphTooltip1 = findViewById<MultiLineGraphTooltipView>(R.id.ttvShcMlgTooltip1)
            lineGraphTooltip1?.showDot(Color.TRANSPARENT)
            lineGraphTooltip1.setContent(entry.xLabel, entry.yLabel)

            val tvShcTooltipHeader = findViewById<Typography>(R.id.tvShcTooltipHeader)
            tvShcTooltipHeader.visible()
            tvShcTooltipHeader.text = entry.xLabel

            // show first metric tooltip
            try {
                selectedMetrics[Int.ZERO].let { metric ->
                    val hexColor = getLineHexColor(metric.summary.lineColor)
                    val value = getTooltipValue(metric, axisIndex)
                    lineGraphTooltip1.setContent(metric.summary.title, value)
                    lineGraphTooltip1.showDot(Color.parseColor(hexColor))
                }
            } catch (e: IndexOutOfBoundsException) {
                Timber.i(e)
            }

            // show second metric tooltip
            val lineGraphTooltip2 = findViewById<MultiLineGraphTooltipView>(R.id.ttvShcMlgTooltip2)
            try {
                selectedMetrics.getOrNull(Int.ONE)?.let { metric ->
                    val hexColor = getLineHexColor(metric.summary.lineColor)
                    val value = getTooltipValue(metric, axisIndex)
                    lineGraphTooltip2.visible()
                    lineGraphTooltip2.setContent(metric.summary.title, value)
                    lineGraphTooltip2.showDot(Color.parseColor(hexColor))
                }
            } catch (e: IndexOutOfBoundsException) {
                lineGraphTooltip2.gone()
                Timber.i(e)
            }
        }
    }

    private fun showComparablePeriodMetricTooltip(
        view: View,
        entry: LineChartEntry,
        axisIndex: Int
    ) {
        with(view) {
            lastSelectedMetric?.let { metric ->
                val lineGraphTooltip1 =
                    findViewById<MultiLineGraphTooltipView>(R.id.ttvShcMlgTooltip1)
                val hexColor = getLineHexColor(metric.summary.lineColor)
                lineGraphTooltip1.showDot(Color.parseColor(hexColor))
                lineGraphTooltip1.setContent(entry.xLabel, entry.yLabel)

                // show current period tooltip
                try {
                    metric.linePeriod.currentPeriod[axisIndex].let {
                        lineGraphTooltip1.showDot(Color.parseColor(hexColor))
                        lineGraphTooltip1.setContent(it.xLabel, it.yLabel)
                    }
                } catch (e: IndexOutOfBoundsException) {
                    Timber.i(e)
                }

                // show last period tooltip
                val ttvShcMlgTooltip2 =
                    findViewById<MultiLineGraphTooltipView>(R.id.ttvShcMlgTooltip2)
                try {
                    metric.linePeriod.lastPeriod[axisIndex].let {
                        ttvShcMlgTooltip2.visible()
                        ttvShcMlgTooltip2.showDot(Color.parseColor(hexColor), true)
                        ttvShcMlgTooltip2.setContent(it.xLabel, it.yLabel)
                    }
                } catch (e: IndexOutOfBoundsException) {
                    ttvShcMlgTooltip2.gone()
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
        val isSingleMetric = metrics.size == Int.ONE

        // map the `current` and `lastPeriod`
        if (isSingleMetric) {
            val metric = metrics[Int.ZERO]

            if (metric.isError) {
                showMetricErrorState()
                return emptyList()
            }

            if (metric.linePeriod.lastPeriod.isNotEmpty()) {
                isMetricComparableByPeriodSelected = true
                showLegendView()
                return getLineChartDataByPeriod(metric)
            } else {
                isMetricComparableByPeriodSelected = false
            }
        } else {
            isMetricComparableByPeriodSelected = false
        }

        hideLegendView()

        val successStateMetrics = metrics.filter { !it.isError }
        if (successStateMetrics.isEmpty()) {
            showMetricErrorState()
            return emptyList()
        }

        clearMetricErrorState()

        return successStateMetrics.map { metric ->
            val hexColor = getLineHexColor(metric.summary.lineColor)
            val chartEntry: List<LineChartEntry> =
                getLineChartEntry(metric.linePeriod.currentPeriod)
            val yAxisLabel = getYAxisLabel(metric)

            return@map LineChartData(
                chartEntry = chartEntry,
                yAxisLabel = yAxisLabel,
                config = LineChartEntryConfigModel(
                    lineWidth = LINE_WIDTH_BOLD,
                    drawFillEnabled = false,
                    lineColor = Color.parseColor(hexColor)
                )
            )
        }
    }

    private fun showMetricErrorState() {
        binding.chartViewShcMultiLine.gone()
        binding.shcMultiLineGraphErrorView.gone()
        binding.shcMultiLineGraphErrorView.visible()
    }

    private fun clearMetricErrorState() {
        binding.chartViewShcMultiLine.visible()
        binding.shcMultiLineGraphErrorView.gone()
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
                    lineWidth = if (isLastPeriod) {
                        LINE_WIDTH_NORMAL
                    } else {
                        LINE_WIDTH_BOLD
                    },
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
        return hexColor.ifBlank {
            ChartColor.DMS_DEFAULT_LINE_COLOR
        }
    }

    private fun getYAxisLabel(metric: MultiLineMetricUiModel): List<AxisLabel> {
        return metric.yAxis.map {
            AxisLabel(it.yValue, it.yLabel)
        }
    }

    private fun View?.animatePop(from: Float, to: Float): ValueAnimator {
        val animator = ValueAnimator.ofFloat(from, to)
        animator.duration = ANIMATION_DURATION.toLong()
        animator.addUpdateListener { valueAnimator ->
            this?.context?.let {
                scaleX = (valueAnimator.animatedValue as? Float).orZero()
                scaleY = (valueAnimator.animatedValue as? Float).orZero()
            }
        }
        animator.start()
        return animator
    }

    private fun animateShowEmptyState() {
        with(emptyStateBinding) {
            if (hideAnimation?.isRunning == true) hideAnimation?.end()
            multiLineEmptyState.show()
            showAnimation = multiLineEmptyState.animatePop(ANIMATION_START, ANIMATION_END)
        }
    }

    private fun animateHideEmptyState() {
        with(emptyStateBinding) {
            if (showAnimation?.isRunning == true) showAnimation?.end()
            if (!multiLineEmptyState.isVisible) return
            hideAnimation = multiLineEmptyState.animatePop(ANIMATION_END, ANIMATION_START)
            hideAnimation?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    multiLineEmptyState.gone()
                    hideAnimation?.removeListener(this)
                }

                override fun onAnimationCancel(animation: Animator) {
                    hideAnimation?.removeListener(this)
                }

                override fun onAnimationStart(animation: Animator) {}
            })
        }
    }

    interface Listener : BaseViewHolderListener {
        fun sendMultiLineGraphImpressionEvent(element: MultiLineGraphWidgetUiModel) {}

        fun sendMultiLineGraphMetricClick(
            element: MultiLineGraphWidgetUiModel,
            metric: MultiLineMetricUiModel
        ) {
        }

        fun sendMultiLineGraphCtaClick(element: MultiLineGraphWidgetUiModel) {}

        fun sendMultiLineGraphEmptyStateCtaClick(element: MultiLineGraphWidgetUiModel) {}
    }
}
