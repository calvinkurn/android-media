package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.charts.common.ChartTooltip
import com.tokopedia.charts.config.BarChartConfig
import com.tokopedia.charts.model.*
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.utils.ChartXAxisLabelFormatter
import com.tokopedia.sellerhomecommon.utils.ChartYAxisLabelFormatter
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.shc_bar_chart_widget.view.*
import kotlinx.android.synthetic.main.shc_partial_chart_tooltip.view.*
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_error.view.*
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_loading.view.*
import kotlinx.android.synthetic.main.shc_partial_line_graph_state_empty.view.*

/**
 * Created By @ilhamsuaib on 09/07/20
 */

class BarChartViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<BarChartWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_bar_chart_widget
    }

    private val emptyState: CardUnify? = itemView?.findViewById(R.id.bar_chart_empty_state)
    private val emptyStateTitle: Typography? = itemView?.findViewById(R.id.tv_shc_bar_chart_empty_state_title)
    private val emptyStateDesc: Typography? = itemView?.findViewById(R.id.tv_shc_bar_chart_empty_state_desc)
    private val emptyStateCta: Typography? = itemView?.findViewById(R.id.tv_shc_bar_chart_empty_state_cta)

    private var showAnimation: ValueAnimator? = null
    private var hideAnimation: ValueAnimator? = null
    private var showEmptyState: Boolean = false

    override fun bind(element: BarChartWidgetUiModel) {

        observeState(element)
    }

    private fun observeState(element: BarChartWidgetUiModel) {
        showAnimation?.end()
        hideAnimation?.end()
        with(itemView) {
            tvShcBarChartTitle.text = element.title
        }

        setupTooltip(element)

        val data = element.data

        when {
            data == null -> setOnLoading()
            data.error.isNotBlank() -> {
                setonError(element)
                listener.setOnErrorWidget(adapterPosition, element, data.error)
            }
            else -> setOnSuccess(element)
        }
    }

    private fun setOnLoading() {
        with(itemView) {
            shimmerWidgetCommon.visible()
            commonWidgetErrorState.gone()
            tvShcBarChartValue.gone()
            tvShcBarChartSubValue.gone()
            barChartShc.gone()
            emptyState?.gone()
        }
    }

    private fun setonError(element: BarChartWidgetUiModel) {
        with(itemView) {
            shimmerWidgetCommon.gone()
            commonWidgetErrorState.visible()
            tvShcBarChartValue.gone()
            tvShcBarChartSubValue.gone()
            barChartShc.gone()
            emptyState?.gone()

            ImageHandler.loadImageWithId(imgWidgetOnError, com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
        }
    }

    private fun setOnSuccess(element: BarChartWidgetUiModel) {
        with(itemView) {
            shimmerWidgetCommon.gone()
            commonWidgetErrorState.gone()

            showEmptyState = showEmpty(element)

            showBarChart(element)

            if (showEmptyState) {
                setupEmptyState(element)
            } else {
                animateHideEmptyState()
            }
        }
    }

    private fun getBarChartConfig(element: BarChartWidgetUiModel): BarChartConfigModel {
        val labelTextColor = itemView.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
        val data = getBarChartData(element.data?.chartData)
        return BarChartConfig.create {
            xAnimationDuration { 200 }
            yAnimationDuration { 200 }
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
                        view.tvShcTooltipTitle.text = it.xLabel
                        view.tvShcTooltipValue.text = it.yLabel
                    }
                }
    }

    private fun openAppLink(appLink: String, dataKey: String, value: String) {
        RouteManager.route(itemView.context, appLink)
    }

    private fun getBarChartData(data: BarChartUiModel?): BarChartData {
        return BarChartData(
                yAxis = getAxisLabels(data?.yAxis),
                xAxisLabels = getAxisLabels(data?.xAxis),
                metrics = getBarChartMetric(data?.metrics, data?.xAxis.orEmpty())
        )
    }

    private fun getBarChartMetric(metrics: List<BarChartMetricsUiModel>?, xLabels: List<BarChartAxisUiModel>): List<BarChartMetric> {
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

    private fun setupTooltip(element: BarChartWidgetUiModel) = with(itemView) {
        val tooltip = element.tooltip
        val shouldShowTooltip = (tooltip?.shouldShow == true) && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
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
        with(itemView) {
            tvShcBarChartValue.visible()
            tvShcBarChartSubValue.visible()
            barChartShc.visible()

            tvShcBarChartValue.text = element.data?.chartData?.summary?.valueFmt.orEmpty()
            tvShcBarChartSubValue.text = element.data?.chartData?.summary?.diffPercentageFmt.orEmpty().parseAsHtml()

            barChartShc.init(getBarChartConfig(element))
            barChartShc.setData(getBarChartData(data?.chartData))
            barChartShc.invalidateChart()

            val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank() && isShown
            val ctaVisibility = if (isCtaVisible) View.VISIBLE else View.GONE
            btnShcBarChartMore.visibility = ctaVisibility
            btnShcBarChartNext.visibility = ctaVisibility
            btnShcBarChartMore.text = element.ctaText

            if (isCtaVisible) {
                btnShcBarChartMore.setOnClickListener {
                    openAppLink(element.appLink, element.dataKey, element.data?.chartData?.summary?.valueFmt.orEmpty())
                }
                btnShcBarChartNext.setOnClickListener {
                    openAppLink(element.appLink, element.dataKey, element.data?.chartData?.summary?.valueFmt.orEmpty())
                }
            }

            addOnImpressionListener(element.impressHolder) {
                listener.sendBarChartImpressionEvent(element)
            }
        }
    }

    private fun showEmpty(element: BarChartWidgetUiModel?): Boolean {
        return element != null && element.isShowEmpty && element.emptyState.title.isNotBlank() && element.emptyState.description.isNotBlank() &&
                element.emptyState.ctaText.isNotBlank() && element.emptyState.appLink.isNotBlank()
    }

    private fun setupEmptyState(element: BarChartWidgetUiModel) {
        with(element.emptyState) {
            emptyStateTitle?.text = title
            emptyStateDesc?.text = description
            emptyStateCta?.run {
                text = ctaText
                setOnClickListener {
                    if (RouteManager.route(itemView.context, appLink)) {
                        listener.sendBarChartEmptyStateCtaClick(element)
                    }
                }
            }
            animateShowEmptyState()
        }
    }

    private fun View?.animatePop(from: Float, to: Float): ValueAnimator {
        val animator = ValueAnimator.ofFloat(from, to)
        animator.duration = 200L
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
        if (emptyState?.isVisible == true) return
        emptyState?.show()
        showAnimation = emptyState?.animatePop(0f, 1f)
    }

    private fun animateHideEmptyState() {
        if (showAnimation?.isRunning == true) showAnimation?.end()
        if (emptyState?.isVisible != true) return
        hideAnimation = emptyState.animatePop(1f, 0f)
        hideAnimation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                emptyState.gone()
                hideAnimation?.removeListener(this)
            }

            override fun onAnimationCancel(animation: Animator?) {
                hideAnimation?.removeListener(this)
            }

            override fun onAnimationStart(animation: Animator?) {}
        })
    }

    interface Listener : BaseViewHolderListener {

        fun sendBarChartImpressionEvent(model: BarChartWidgetUiModel) {}
        fun sendBarChartEmptyStateCtaClick(element: BarChartWidgetUiModel) {}

    }
}