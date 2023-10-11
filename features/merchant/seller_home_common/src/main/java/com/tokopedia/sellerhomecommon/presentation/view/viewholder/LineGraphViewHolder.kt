package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.charts.common.ChartTooltip
import com.tokopedia.charts.config.LineChartConfig
import com.tokopedia.charts.model.AxisLabel
import com.tokopedia.charts.model.LineChartConfigModel
import com.tokopedia.charts.model.LineChartData
import com.tokopedia.charts.model.LineChartEntry
import com.tokopedia.charts.model.LineChartEntryConfigModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcLineGraphWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.ChartXAxisLabelFormatter
import com.tokopedia.sellerhomecommon.utils.ChartYAxisLabelFormatter
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class LineGraphViewHolder(
    view: View?,
    private val listener: Listener
) : AbstractViewHolder<LineGraphWidgetUiModel>(view) {

    companion object {
        @LayoutRes
        val RES_LAYOUT: Int = R.layout.shc_line_graph_widget

        @LayoutRes
        private val TOOLTIP_RES_LAYOUT = R.layout.shc_partial_chart_tooltip
        private const val ANIMATION_DURATION = 200
    }

    private val binding by lazy { ShcLineGraphWidgetBinding.bind(itemView) }
    private val emptyStateBinding by lazy { binding.shcLineGraphEmptyState }
    private val loadingStateBinding by lazy { binding.shcLineGraphLoadingState }

    private var showAnimation: ValueAnimator? = null
    private var hideAnimation: ValueAnimator? = null
    private var showEmptyState: Boolean = false

    override fun bind(element: LineGraphWidgetUiModel) = with(binding) {
        showAnimation?.end()
        hideAnimation?.end()
        observeState(element)

        val data = element.data

        tvLineGraphTitle.text = element.title
        tvLineGraphValue.text = data?.header.orEmpty()
        tvLineGraphSubValue.text = data?.description.orEmpty().parseAsHtml()

        setupTooltip(element)
        setTagNotification(element.tag)
    }

    private fun openAppLink(element: LineGraphWidgetUiModel) {
        if (RouteManager.route(itemView.context, element.appLink)) {
            listener.sendLineGraphCtaClickEvent(element)
        }
    }

    /**
     * Loading State -> when data is null. By default when adapter created
     * it will pass null data.
     * Error State -> when errorMessage field have a message
     * else -> state is Success
     * */
    private fun observeState(element: LineGraphWidgetUiModel) {
        val data: LineGraphDataUiModel? = element.data
        itemView.show()
        when {
            null == data || element.showLoadingState -> showLoadingState(element)
            data.error.isNotBlank() -> {
                onStateLoading(false)
                showViewComponent(false, element)
                onStateError(element, true)
                listener.setOnErrorWidget(absoluteAdapterPosition, element, data.error)
            }

            else -> {
                removeIfEmpty(element)
                onStateLoading(false)
                onStateError(element, false)
                showViewComponent(true, element)
            }
        }
    }

    private fun removeIfEmpty(element: LineGraphWidgetUiModel) {
        val shouldRemove = !element.data?.showWidget.orFalse()
        if (shouldRemove) {
            listener.removeWidget(absoluteAdapterPosition, element)
        }
    }

    private fun showLoadingState(element: LineGraphWidgetUiModel) {
        showViewComponent(false, element)
        onStateError(element, false)
        onStateLoading(true)
    }

    private fun setupTooltip(element: LineGraphWidgetUiModel) = with(binding) {
        val tooltip = element.tooltip
        val shouldShowTooltip =
            (tooltip?.shouldShow == true) && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
        if (shouldShowTooltip) {
            tvLineGraphTitle.setUnifyDrawableEnd(IconUnify.INFORMATION)
            tvLineGraphTitle.setOnClickListener {
                listener.onTooltipClicked(tooltip ?: return@setOnClickListener)
            }
        } else {
            tvLineGraphTitle.clearUnifyDrawableEnd()
        }
    }

    private fun onStateLoading(isShown: Boolean) {
        loadingStateBinding.shimmerWidgetCommon.isVisible = isShown
        if (isShown) {
            emptyStateBinding.root.gone()
        }
    }

    private fun onStateError(element: LineGraphWidgetUiModel, isShown: Boolean) {
        binding.shcLineGraphErrorState.isVisible = isShown
        if (isShown) {
            binding.shcLineGraphErrorState.setOnReloadClicked {
                listener.onReloadWidget(element)
            }
            emptyStateBinding.root.gone()
        }
    }

    private fun showViewComponent(isShown: Boolean, element: LineGraphWidgetUiModel) {
        with(binding) {
            val componentVisibility = if (isShown) View.VISIBLE else View.INVISIBLE
            tvLineGraphValue.visibility = componentVisibility
            tvLineGraphSubValue.visibility = componentVisibility
            btnLineGraphMore.visibility = componentVisibility
            lineGraphView.visibility = componentVisibility
            luvShcLineGraph.visibility = componentVisibility

            setupSeeMoreCta(element, isShown)

            if (isShown) {
                setupLastUpdated(element)
                showEmptyState = showEmpty(element)
                showLineGraph(element)
                itemView.addOnImpressionListener(element.impressHolder) {
                    listener.sendLineGraphImpressionEvent(element)
                }
                setupEmptyState(element)
            }

            horLineShcLineGraphBtm.isVisible = btnLineGraphMore.isVisible ||
                    luvShcLineGraph.isVisible
        }
    }

    private fun setupEmptyState(element: LineGraphWidgetUiModel) {
        if (element.data?.isWidgetEmpty().orFalse()) {
            if (element.isShowEmpty) {
                if (element.shouldShowEmptyStateIfEmpty()) {
                    showEmptyState(element)
                } else {
                    animateHideEmptyState()
                }
            } else {
                listener.removeWidget(absoluteAdapterPosition, element)
            }
        } else {
            animateHideEmptyState()
        }
    }

    private fun setupLastUpdated(element: LineGraphWidgetUiModel) {
        element.data?.lastUpdated?.let {
            val shouldShowRefreshBtn = element.data?.lastUpdated?.needToUpdated.orFalse()
            binding.luvShcLineGraph.run {
                isVisible = it.isEnabled
                setLastUpdated(it.lastUpdatedInMillis)
                setRefreshButtonVisibility(shouldShowRefreshBtn)
                setRefreshButtonClickListener {
                    refreshWidget(element)
                }
            }
        }
    }

    private fun refreshWidget(element: LineGraphWidgetUiModel) {
        listener.onReloadWidget(element)
    }

    private fun setupSeeMoreCta(element: LineGraphWidgetUiModel, isShown: Boolean) {
        with(binding) {
            val isCtaVisible =
                element.appLink.isNotBlank() && element.ctaText.isNotBlank() && isShown
            val ctaVisibility = if (isCtaVisible) View.VISIBLE else View.GONE
            btnLineGraphMore.visibility = ctaVisibility

            if (!isCtaVisible) return

            btnLineGraphMore.text = element.ctaText
            btnLineGraphMore.setOnClickListener {
                openAppLink(element)
            }

            val iconColor = root.context.getResColor(
                unifyprinciplesR.color.Unify_GN500
            )
            val iconWidth = root.context.resources.getDimension(
                unifyprinciplesR.dimen.layout_lvl3
            )
            val iconHeight = root.context.resources.getDimension(
                unifyprinciplesR.dimen.layout_lvl3
            )
            btnLineGraphMore.setUnifyDrawableEnd(
                IconUnify.CHEVRON_RIGHT,
                iconColor,
                iconWidth,
                iconHeight
            )
        }
    }

    private fun setTagNotification(tag: String) {
        val isTagVisible = tag.isNotBlank()
        with(binding) {
            notifTagLineGraph.showWithCondition(isTagVisible)
            if (isTagVisible) {
                notifTagLineGraph.setNotification(
                    tag,
                    NotificationUnify.TEXT_TYPE,
                    NotificationUnify.COLOR_TEXT_TYPE
                )
            }
        }
    }

    private fun showEmpty(element: LineGraphWidgetUiModel): Boolean {
        return element.isEmpty() && element.shouldShowEmptyStateIfEmpty() && element.isShowEmpty
    }

    private fun showEmptyState(element: LineGraphWidgetUiModel) {
        with(element.emptyState) {
            emptyStateBinding.root.visible()
            emptyStateBinding.tvLineGraphEmptyStateTitle.text = title
            emptyStateBinding.tvLineGraphEmptyStateDescription.text = description
            emptyStateBinding.tvShcMultiLineEmptyStateCta.text = ctaText
            emptyStateBinding.tvShcMultiLineEmptyStateCta.setOnClickListener {
                listener.sendLineChartEmptyStateCtaClickEvent(element)
                RouteManager.route(itemView.context, appLink)
            }
            animateShowEmptyState()
        }
    }

    private fun showLineGraph(element: LineGraphWidgetUiModel) {
        with(binding.lineGraphView) {
            init(getLineChartConfig(element))
            setDataSets(getLineChartData(element))
            invalidateChart()
        }
    }

    private fun getLineChartData(element: LineGraphWidgetUiModel): LineChartData {
        val chartEntry: List<LineChartEntry> = element.data?.list?.map {
            LineChartEntry(it.yVal, it.yLabel, it.xLabel)
        }.orEmpty()

        val yAxisLabel = getYAxisLabel(element)

        return LineChartData(
            chartEntry = chartEntry,
            yAxisLabel = yAxisLabel,
            config = LineChartEntryConfigModel(
                lineWidth = 1.8f
            )
        )
    }

    private fun getYAxisLabel(element: LineGraphWidgetUiModel): List<AxisLabel> {
        return element.data?.yLabels?.map {
            AxisLabel(it.yVal, it.yLabel)
        }.orEmpty()
    }

    private fun getLineChartConfig(element: LineGraphWidgetUiModel): LineChartConfigModel {
        val lineChartData = getLineChartData(element)
        return LineChartConfig.create {
            xAnimationDuration { ANIMATION_DURATION }
            yAnimationDuration { ANIMATION_DURATION }
            tooltipEnabled { !showEmptyState }
            setChartTooltip(getLineGraphTooltip())

            xAxis {
                val xAxisLabels = lineChartData.chartEntry.map { it.xLabel }
                gridEnabled { false }
                textColor { itemView.context.getResColor(unifyprinciplesR.color.Unify_NN600) }
                labelFormatter {
                    ChartXAxisLabelFormatter(xAxisLabels)
                }
            }

            yAxis {
                val yAxisLabels = lineChartData.yAxisLabel
                textColor { itemView.context.getResColor(unifyprinciplesR.color.Unify_NN600) }
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
                    view.findViewById<Typography>(R.id.tvShcTooltipTitle).text = it.xLabel
                    view.findViewById<Typography>(R.id.tvShcTooltipValue).text = it.yLabel
                }
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
        if (hideAnimation?.isRunning == true) hideAnimation?.end()
        emptyStateBinding.multiLineEmptyState.show()
        showAnimation = emptyStateBinding.multiLineEmptyState.animatePop(0f, 1f)
    }

    private fun animateHideEmptyState() {
        if (showAnimation?.isRunning == true) showAnimation?.end()
        hideAnimation = emptyStateBinding.multiLineEmptyState.animatePop(1f, 0f)
        hideAnimation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                emptyStateBinding.multiLineEmptyState.gone()
                hideAnimation?.removeListener(this)
            }

            override fun onAnimationCancel(animation: Animator) {
                hideAnimation?.removeListener(this)
            }

            override fun onAnimationStart(animation: Animator) {}
        })
    }

    interface Listener : BaseViewHolderListener {

        fun sendLineGraphImpressionEvent(model: LineGraphWidgetUiModel) {}

        fun sendLineGraphCtaClickEvent(model: LineGraphWidgetUiModel) {}

        fun sendLineChartEmptyStateCtaClickEvent(model: LineGraphWidgetUiModel) {}
    }
}
