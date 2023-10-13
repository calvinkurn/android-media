package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.charts.common.ChartTooltip
import com.tokopedia.charts.config.BarChartConfig
import com.tokopedia.charts.model.AxisLabel
import com.tokopedia.charts.model.BarChartConfigModel
import com.tokopedia.charts.model.BarChartData
import com.tokopedia.charts.model.BarChartMetric
import com.tokopedia.charts.model.BarChartMetricValue
import com.tokopedia.iconunify.IconUnify
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
import com.tokopedia.sellerhomecommon.databinding.ShcBarChartWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.BarChartAxisUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartMetricsUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.ChartXAxisLabelFormatter
import com.tokopedia.sellerhomecommon.utils.ChartYAxisLabelFormatter
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.toggleWidgetHeight
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By @ilhamsuaib on 09/07/20
 */

class BarChartViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<BarChartWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_bar_chart_widget
        private const val ANIMATION_DURATION = 200
    }

    private val binding by lazy {
        ShcBarChartWidgetBinding.bind(itemView)
    }
    private val emptyStateBinding by lazy { binding.shcBarChartEmptyState }
    private val loadingStateBinding by lazy { binding.shcBarChartLoadingState }

    private var showAnimation: ValueAnimator? = null
    private var hideAnimation: ValueAnimator? = null
    private var showEmptyState: Boolean = false

    override fun bind(element: BarChartWidgetUiModel) {
        observeState(element)
    }

    private fun observeState(element: BarChartWidgetUiModel) {
        if (!listener.getIsShouldRemoveWidget()) {
            binding.root.toggleWidgetHeight(true)
        }
        showAnimation?.end()
        hideAnimation?.end()
        with(binding) {
            tvShcBarChartTitle.text = element.title
        }

        setupTooltip(element)
        setTagNotification(element.tag)

        val data = element.data

        when {
            data == null || element.showLoadingState -> setOnLoading()
            data.error.isNotBlank() -> {
                setonError(element)
                listener.setOnErrorWidget(absoluteAdapterPosition, element, data.error)
            }
            else -> setOnSuccess(element)
        }
    }

    private fun setOnLoading() {
        with(binding) {
            loadingStateBinding.shimmerWidgetCommon.visible()
            shcBarChartErrorState.gone()
            tvShcBarChartValue.gone()
            tvShcBarChartSubValue.gone()
            barChartShc.gone()
            luvShcBarChart.gone()
            emptyStateBinding.shcBarChartEmptyState.gone()
        }
    }

    private fun setonError(element: BarChartWidgetUiModel) {
        with(binding) {
            loadingStateBinding.shimmerWidgetCommon.gone()
            shcBarChartErrorState.visible()
            tvShcBarChartValue.gone()
            tvShcBarChartSubValue.gone()
            barChartShc.gone()
            luvShcBarChart.gone()
            emptyStateBinding.shcBarChartEmptyState.gone()
            shcBarChartErrorState.setOnReloadClicked {
                listener.onReloadWidget(element)
            }
        }
    }

    private fun setOnSuccess(element: BarChartWidgetUiModel) {
        loadingStateBinding.shimmerWidgetCommon.gone()
        binding.shcBarChartErrorState.gone()

        showEmptyState = showEmpty(element)

        showBarChart(element)

        if (element.isEmpty()) {
            if (element.isShowEmpty) {
                if (element.shouldShowEmptyStateIfEmpty()) {
                    setupEmptyState(element)
                } else {
                    animateHideEmptyState()
                }
                setupLastUpdatedInfo(element)
            } else {
                if (listener.getIsShouldRemoveWidget()) {
                    listener.removeWidget(absoluteAdapterPosition, element)
                } else {
                    listener.onRemoveWidget(absoluteAdapterPosition)
                    itemView.toggleWidgetHeight(false)
                }
            }
        } else {
            animateHideEmptyState()
            setupLastUpdatedInfo(element)
        }

        setBottomGuideLineVisibility()
    }

    private fun setBottomGuideLineVisibility() {
        with(binding) {
            horLineShcBarChartBtm.isVisible = luvShcBarChart.isVisible ||
                btnShcBarChartMore.isVisible
        }
    }

    private fun setupLastUpdatedInfo(element: BarChartWidgetUiModel) {
        binding.luvShcBarChart.run {
            element.data?.lastUpdated?.let { lastUpdated ->
                isVisible = lastUpdated.isEnabled
                setLastUpdated(lastUpdated.lastUpdatedInMillis)
                setRefreshButtonVisibility(lastUpdated.needToUpdated)
                setRefreshButtonClickListener {
                    refreshWidget(element)
                }
            }
        }
    }

    private fun refreshWidget(element: BarChartWidgetUiModel) {
        listener.onReloadWidget(element)
    }

    private fun setTagNotification(tag: String) {
        val isTagVisible = tag.isNotBlank()
        with(binding) {
            notifTagBarChart.showWithCondition(isTagVisible)
            if (isTagVisible) {
                notifTagBarChart.setNotification(
                    tag,
                    NotificationUnify.TEXT_TYPE,
                    NotificationUnify.COLOR_TEXT_TYPE
                )
            }
        }
    }

    private fun getBarChartConfig(element: BarChartWidgetUiModel): BarChartConfigModel {
        val labelTextColor =
            itemView.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN900)
        val data = getBarChartData(element.data?.chartData)
        return BarChartConfig.create {
            xAnimationDuration { ANIMATION_DURATION }
            yAnimationDuration { ANIMATION_DURATION }
            barBorderRadius { itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1) }

            xAxis {
                val xAxisLabels = data.xAxisLabels.map { it.valueFmt }
                gridEnabled { false }
                textColor { labelTextColor }
                labelFormatter {
                    ChartXAxisLabelFormatter(xAxisLabels)
                }
            }

            yAxis {
                val yAxisLabels = data.yAxis
                textColor { labelTextColor }
                labelCount { yAxisLabels.size }
                labelFormatter {
                    ChartYAxisLabelFormatter(yAxisLabels)
                }
            }

            tooltipEnabled { !showEmptyState }
            setChartTooltip(getBarChartTooltip())
        }
    }

    private fun getBarChartTooltip(): ChartTooltip {
        return ChartTooltip(itemView.context, R.layout.shc_partial_chart_tooltip)
            .setOnDisplayContent { view, data, x, y ->
                (data as? BarChartMetricValue)?.let {
                    view.findViewById<Typography>(R.id.tvShcTooltipTitle).text = it.xLabel
                    view.findViewById<Typography>(R.id.tvShcTooltipValue).text = it.yLabel
                }
            }
    }

    private fun openAppLink(appLink: String) {
        RouteManager.route(itemView.context, appLink)
    }

    private fun getBarChartData(data: BarChartUiModel?): BarChartData {
        return BarChartData(
            yAxis = getAxisLabels(data?.yAxis),
            xAxisLabels = getAxisLabels(data?.xAxis),
            metrics = getBarChartMetric(data?.metrics, data?.xAxis.orEmpty())
        )
    }

    private fun getBarChartMetric(
        metrics: List<BarChartMetricsUiModel>?,
        xLabels: List<BarChartAxisUiModel>
    ): List<BarChartMetric> {
        return metrics?.map {
            BarChartMetric(
                title = it.title,
                barHexColor = it.barHexColor,
                values = it.value.mapIndexed { i, item ->
                    val xLabel = getXLabel(xLabels, i)
                    return@mapIndexed BarChartMetricValue(item.value, item.valueFmt, xLabel)
                }
            )
        }.orEmpty()
    }

    private fun getXLabel(xLabels: List<BarChartAxisUiModel>, index: Int): String {
        return try {
            xLabels[index].valueFmt
        } catch (e: IndexOutOfBoundsException) {
            ""
        }
    }

    private fun getAxisLabels(axisList: List<BarChartAxisUiModel>?): List<AxisLabel> {
        return axisList?.map { AxisLabel(it.value.toFloat(), it.valueFmt) }.orEmpty()
    }

    private fun setupTooltip(element: BarChartWidgetUiModel) = with(binding) {
        val tooltip = element.tooltip
        val shouldShowTooltip =
            (tooltip?.shouldShow == true) && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
        if (shouldShowTooltip) {
            tvShcBarChartTitle.setUnifyDrawableEnd(IconUnify.INFORMATION)
            tvShcBarChartTitle.setOnClickListener {
                listener.onTooltipClicked(tooltip ?: return@setOnClickListener)
            }
        } else {
            tvShcBarChartTitle.clearUnifyDrawableEnd()
        }
    }

    private fun showBarChart(element: BarChartWidgetUiModel) {
        val data = element.data
        with(binding) {
            tvShcBarChartValue.visible()
            tvShcBarChartSubValue.visible()
            barChartShc.visible()

            tvShcBarChartValue.text = element.data?.chartData?.summary?.valueFmt.orEmpty()
            tvShcBarChartSubValue.text =
                element.data?.chartData?.summary?.diffPercentageFmt.orEmpty().parseAsHtml()

            barChartShc.init(getBarChartConfig(element))
            barChartShc.setData(getBarChartData(data?.chartData))
            barChartShc.invalidateChart()

            setupSeeMoreCta(element)

            root.addOnImpressionListener(element.impressHolder) {
                listener.sendBarChartImpressionEvent(element)
            }
        }
    }

    private fun setupSeeMoreCta(element: BarChartWidgetUiModel) {
        with(binding) {
            val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank()
            val ctaVisibility = if (isCtaVisible) View.VISIBLE else View.GONE
            btnShcBarChartMore.visibility = ctaVisibility
            btnShcBarChartMore.text = element.ctaText

            if (isCtaVisible) {
                val iconColor = root.context.getResColor(
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
                val iconWidth = root.context.resources.getDimension(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
                )
                val iconHeight = root.context.resources.getDimension(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
                )
                btnShcBarChartMore.setUnifyDrawableEnd(
                    IconUnify.CHEVRON_RIGHT,
                    iconColor,
                    iconWidth,
                    iconHeight
                )
                btnShcBarChartMore.setOnClickListener {
                    onSeeMoreClicked(element)
                }
            }
        }
    }

    private fun onSeeMoreClicked(element: BarChartWidgetUiModel) {
        listener.sendBarChartSeeMoreClickEvent(element)
        openAppLink(element.appLink)
    }

    private fun showEmpty(element: BarChartWidgetUiModel): Boolean {
        return element.isEmpty() && element.shouldShowEmptyStateIfEmpty() && element.isShowEmpty
    }

    private fun setupEmptyState(element: BarChartWidgetUiModel) {
        with(element.emptyState) {
            emptyStateBinding.tvShcBarChartEmptyStateTitle.text = title
            emptyStateBinding.tvShcBarChartEmptyStateDesc.text = description
            emptyStateBinding.tvShcBarChartEmptyStateCta.run {
                text = ctaText
                setOnClickListener {
                    if (RouteManager.route(itemView.context, appLink)) {
                        listener.sendBarChartEmptyStateCtaClick(element)
                    }
                }
            }
            setMarginFromLongestYAxisValue(element)
            animateShowEmptyState()
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
        if (emptyStateBinding.shcBarChartEmptyState.isVisible) return
        emptyStateBinding.shcBarChartEmptyState.show()
        showAnimation = emptyStateBinding.shcBarChartEmptyState.animatePop(0f, 1f)
    }

    private fun animateHideEmptyState() {
        if (showAnimation?.isRunning == true) showAnimation?.end()
        if (!emptyStateBinding.shcBarChartEmptyState.isVisible) return
        hideAnimation = emptyStateBinding.shcBarChartEmptyState.animatePop(1f, 0f)
        hideAnimation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                emptyStateBinding.shcBarChartEmptyState.gone()
                hideAnimation?.removeListener(this)
            }

            override fun onAnimationCancel(animation: Animator) {
                hideAnimation?.removeListener(this)
            }

            override fun onAnimationStart(animation: Animator) {}
        })
    }

    /**
     * Set left margin of empty state dynamically to avoid blocking the Y Axis
     *
     * @param element bar chart widget ui model
     */
    private fun setMarginFromLongestYAxisValue(element: BarChartWidgetUiModel) {
        element.data?.chartData?.yAxis?.lastOrNull()?.valueFmt?.length?.let { valueLength ->
            val pxPerChar =
                itemView.resources?.getDimensionPixelOffset(R.dimen.shc_dimen_6dp).orZero()
            val startMargin =
                itemView.resources?.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
                    .orZero()
            val textMarginPx = pxPerChar * valueLength
            val emptyStateLayoutParams =
                (emptyStateBinding.shcBarChartEmptyState.layoutParams as? ConstraintLayout.LayoutParams)?.apply {
                    setMargins(startMargin + textMarginPx, topMargin, rightMargin, bottomMargin)
                }
            emptyStateLayoutParams?.let {
                emptyStateBinding.shcBarChartEmptyState.layoutParams = it
            }
        }
    }

    interface Listener : BaseViewHolderListener {

        fun sendBarChartImpressionEvent(model: BarChartWidgetUiModel) {}
        fun sendBarChartEmptyStateCtaClick(model: BarChartWidgetUiModel) {}
        fun sendBarChartSeeMoreClickEvent(model: BarChartWidgetUiModel) {}
    }
}
