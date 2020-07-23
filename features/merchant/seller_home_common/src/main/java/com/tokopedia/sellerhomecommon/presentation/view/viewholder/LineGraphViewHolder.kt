package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.charts.common.ChartTooltip
import com.tokopedia.charts.config.LineChartConfig
import com.tokopedia.charts.model.AxisLabel
import com.tokopedia.charts.model.LineChartConfigModel
import com.tokopedia.charts.model.LineChartData
import com.tokopedia.charts.model.LineChartEntry
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphWidgetUiModel
import kotlinx.android.synthetic.main.shc_line_graph_widget.view.*
import kotlinx.android.synthetic.main.shc_partial_chart_tooltip.view.*
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_error.view.*
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_loading.view.*

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

    override fun bind(element: LineGraphWidgetUiModel) = with(itemView) {
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
        if (!tooltip?.content.isNullOrBlank() || !tooltip?.list.isNullOrEmpty()) {
            btnLineGraphInformation.visible()
            tvLineGraphTitle.setOnClickListener {
                listener.onTooltipClicked(tooltip ?: return@setOnClickListener)
            }
            btnLineGraphInformation.setOnClickListener {
                listener.onTooltipClicked(tooltip ?: return@setOnClickListener)
            }
        } else {
            btnLineGraphInformation.gone()
        }
    }

    private fun onStateLoading(isShown: Boolean) = with(itemView) {
        shimmerWidgetCommon.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    private fun onStateError(isShown: Boolean) = with(itemView) {
        ImageHandler.loadImageWithId(imgWidgetOnError, R.drawable.unify_globalerrors_connection)
        commonWidgetErrorState.visibility = if (isShown) View.VISIBLE else View.GONE
        btnLineGraphInformation.visibility = if (isShown) View.VISIBLE else View.INVISIBLE
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
            showLineGraph(element)
            itemView.addOnImpressionListener(element.impressHolder) {
                listener.sendLineGraphImpressionEvent(element.dataKey, element.data?.header.orEmpty())
            }
        }
    }

    private fun showLineGraph(element: LineGraphWidgetUiModel) {
        with(itemView.lineGraphView) {
            init(getLineChartConfig())
            setData(getLineChartData(element))
            invalidateChart()
        }
    }

    private fun getLineChartData(element: LineGraphWidgetUiModel): LineChartData {
        val chartEntry: List<LineChartEntry> = element.data?.list?.map {
            LineChartEntry(it.yVal, it.yLabel, it.xLabel)
        }.orEmpty()

        val yAxisLabel = element.data?.yLabels?.map {
            AxisLabel(it.yVal, it.yLabel)
        }.orEmpty()

        return LineChartData(
                chartEntry = chartEntry,
                yAxisLabel = yAxisLabel
        )
    }

    private fun getLineChartConfig(): LineChartConfigModel {
        return LineChartConfig.create {
            xAnimationDuration { 200 }
            yAnimationDuration { 200 }
            tooltipEnabled { true }
            setChartTooltip(getLineGraphTooltip())
            xAxis {
                gridEnabled { false }
                textColor { itemView.context.getResColor(R.color.Neutral_N700_96) }
            }
            yAxis {
                textColor { itemView.context.getResColor(R.color.Neutral_N700_96) }
            }
            chartLineWidth { 1.8f }
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

    interface Listener : BaseViewHolderListener {

        fun sendLineGraphImpressionEvent(dataKey: String, chartValue: String) {}

        fun sendLineGraphCtaClickEvent(dataKey: String, chartValue: String) {}
    }
}