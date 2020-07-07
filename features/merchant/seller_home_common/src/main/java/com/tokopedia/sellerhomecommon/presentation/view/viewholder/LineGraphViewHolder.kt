package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.charts.config.linechart.LineChartConfigBuilder
import com.tokopedia.charts.config.linechart.LineChartTooltip
import com.tokopedia.charts.config.linechart.model.LineChartConfig
import com.tokopedia.charts.model.LineChartEntry
import com.tokopedia.charts.model.YAxisLabel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphWidgetUiModel
import kotlinx.android.synthetic.main.shc_line_graph_widget.view.*
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_error.view.*
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_loading.view.*
import kotlinx.android.synthetic.main.shc_partial_line_graph_tooltip.view.*

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
        private val TOOLTIP_RES_LAYOUT = R.layout.shc_partial_line_graph_tooltip
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
        val dataSet: List<LineChartEntry> = element.data?.list?.map {
            LineChartEntry(it.yVal.toFloat(), it.yLabel, it.xLabel)
        }.orEmpty()

        with(itemView.lineGraphView) {
            init(getLineChartConfig())
            setCustomYAxisLabel(getCustomYAxisLabel(element))
            setData(dataSet)
            invalidateChart()
        }
    }

    private fun getLineChartConfig(): LineChartConfig {
        return LineChartConfigBuilder.create {
            showTooltipEnabled { true }
            setTooltip(getLineGraphTooltip())
            xAnimationDuration { 200 }
            yAnimationDuration { 200 }
        }
    }

    private fun getLineGraphTooltip(): LineChartTooltip {
        return LineChartTooltip(itemView.context, TOOLTIP_RES_LAYOUT)
                .setOnDisplayContent { view, data, x, y ->
                    (data as? LineChartEntry)?.let {
                        view.tvTitle.text = it.xLabel
                        view.tvValue.text = it.yLabel
                    }
                }
    }

    private fun getCustomYAxisLabel(element: LineGraphWidgetUiModel): List<YAxisLabel> {
        return element.data?.yLabels?.map { YAxisLabel(it.yVal.toFloat(), it.yLabel) }.orEmpty()
    }

    interface Listener : BaseViewHolderListener {
        fun getLineGraphData() {}

        fun sendLineGraphImpressionEvent(dataKey: String, cardValue: String) {}

        fun sendLineGraphCtaClickEvent(dataKey: String, cardValue: String) {}
    }
}