package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.charts.common.ChartTooltip
import com.tokopedia.charts.config.LineChartConfig
import com.tokopedia.charts.model.*
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetEmptyStateUiModel
import com.tokopedia.sellerhomecommon.utils.ChartXAxisLabelFormatter
import com.tokopedia.sellerhomecommon.utils.ChartYAxisLabelFormatter
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import kotlinx.android.synthetic.main.shc_line_graph_widget.view.*
import kotlinx.android.synthetic.main.shc_partial_chart_tooltip.view.*
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_error.view.*
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_loading.view.*
import kotlinx.android.synthetic.main.shc_partial_line_graph_state_empty.view.*

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
    }

    private var showAnimation: ValueAnimator? = null
    private var hideAnimation: ValueAnimator? = null
    private var showEmptyState: Boolean = false

    override fun bind(element: LineGraphWidgetUiModel) = with(itemView) {
        showAnimation?.end()
        hideAnimation?.end()
        observeState(element)

        val data = element.data

        tvLineGraphTitle.text = element.title
        tvLineGraphValue.text = data?.header.orEmpty()
        tvLineGraphSubValue.text = data?.description.orEmpty().parseAsHtml()

        setupTooltip(element)
    }

    private fun openAppLink(appLink: String, dataKey: String, value: String) {
        if (RouteManager.route(itemView.context, appLink)) {
            listener.sendLineGraphCtaClickEvent(dataKey, value)
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
        when {
            null == data -> {
                showViewComponent(false, element)
                onStateError(false)
                onStateLoading(true)
            }
            data.error.isNotBlank() -> {
                onStateLoading(false)
                showViewComponent(false, element)
                onStateError(true)
                listener.setOnErrorWidget(adapterPosition, element)
            }
            else -> {
                onStateLoading(false)
                onStateError(false)
                showViewComponent(true, element)
            }
        }
    }

    private fun setupTooltip(element: LineGraphWidgetUiModel) = with(itemView) {
        val tooltip = element.tooltip
        val shouldShowTooltip = (tooltip?.shouldShow == true) && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
        if (shouldShowTooltip) {
            tvLineGraphTitle.setUnifyDrawableEnd(IconUnify.INFORMATION)
            tvLineGraphTitle.setOnClickListener {
                listener.onTooltipClicked(tooltip ?: return@setOnClickListener)
            }
        } else {
            tvLineGraphTitle.clearUnifyDrawableEnd()
        }
    }

    private fun onStateLoading(isShown: Boolean) = with(itemView) {
        shimmerWidgetCommon.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    private fun onStateError(isShown: Boolean) = with(itemView) {
        ImageHandler.loadImageWithId(imgWidgetOnError, com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
        commonWidgetErrorState.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    private fun showViewComponent(isShown: Boolean, element: LineGraphWidgetUiModel) = with(itemView) {
        val componentVisibility = if (isShown) View.VISIBLE else View.INVISIBLE
        tvLineGraphValue.visibility = componentVisibility
        tvLineGraphSubValue.visibility = componentVisibility
        btnLineGraphMore.visibility = componentVisibility
        btnLineGraphNext.visibility = componentVisibility
        lineGraphView.visibility = componentVisibility

        val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank() && isShown
        val ctaVisibility = if (isCtaVisible) View.VISIBLE else View.GONE
        btnLineGraphMore.visibility = ctaVisibility
        btnLineGraphNext.visibility = ctaVisibility
        btnLineGraphMore.text = element.ctaText

        if (isCtaVisible) {
            btnLineGraphMore.setOnClickListener {
                openAppLink(element.appLink, element.dataKey, element.data?.header.orEmpty())
            }
            btnLineGraphNext.setOnClickListener {
                openAppLink(element.appLink, element.dataKey, element.data?.header.orEmpty())
            }
        }

        if (isShown) {
            showEmptyState = showEmpty(element)
            showLineGraph(element)
            itemView.addOnImpressionListener(element.impressHolder) {
                listener.sendLineGraphImpressionEvent(element)
            }
            if (showEmptyState) {
                showEmptyState = true
                setupEmptyState(element.emptyState)
            } else {
                animateHideEmptyState()
            }
        }
    }

    private fun showEmpty(element: LineGraphWidgetUiModel): Boolean {
        return element.isShowEmpty && element.data?.list?.all { it.yVal == 0f } == true &&
                element.emptyState.title.isNotBlank() && element.emptyState.description.isNotBlank() &&
                element.emptyState.ctaText.isNotBlank() && element.emptyState.appLink.isNotBlank()
    }

    private fun setupEmptyState(emptyState: WidgetEmptyStateUiModel) {
        with(emptyState) {
            itemView.tvLineGraphEmptyStateTitle.text = title
            itemView.tvLineGraphEmptyStateDescription.text = description
            itemView.tvShcMultiLineEmptyStateCta.text = ctaText
            itemView.tvShcMultiLineEmptyStateCta.setOnClickListener {
                RouteManager.route(itemView.context, appLink)
            }
            animateShowEmptyState()
        }
    }

    private fun showLineGraph(element: LineGraphWidgetUiModel) {
        with(itemView.lineGraphView) {
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
            xAnimationDuration { 200 }
            yAnimationDuration { 200 }
            tooltipEnabled { !showEmptyState }
            setChartTooltip(getLineGraphTooltip())

            xAxis {
                val xAxisLabels = lineChartData.chartEntry.map { it.xLabel }
                gridEnabled { false }
                textColor { itemView.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_96) }
                labelFormatter {
                    ChartXAxisLabelFormatter(xAxisLabels)
                }
            }

            yAxis {
                val yAxisLabels = lineChartData.yAxisLabel
                textColor { itemView.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_96) }
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
        itemView.multiLineEmptyState.show()
        showAnimation = itemView.multiLineEmptyState.animatePop(0f, 1f)
    }

    private fun animateHideEmptyState() {
        if (showAnimation?.isRunning == true) showAnimation?.end()
        hideAnimation = itemView.multiLineEmptyState.animatePop(1f, 0f)
        hideAnimation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                itemView.multiLineEmptyState?.gone()
                hideAnimation?.removeListener(this)
            }

            override fun onAnimationCancel(animation: Animator?) {
                hideAnimation?.removeListener(this)
            }

            override fun onAnimationStart(animation: Animator?) {}
        })
    }

    interface Listener : BaseViewHolderListener {

        fun sendLineGraphImpressionEvent(model: LineGraphWidgetUiModel) {}

        fun sendLineGraphCtaClickEvent(dataKey: String, chartValue: String) {}
    }
}