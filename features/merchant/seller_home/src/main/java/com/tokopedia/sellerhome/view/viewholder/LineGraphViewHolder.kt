package com.tokopedia.sellerhome.view.viewholder

import android.graphics.Color
import android.view.View
import com.db.williamchart.Tools
import com.db.williamchart.base.BaseWilliamChartConfig
import com.db.williamchart.base.BaseWilliamChartModel
import com.db.williamchart.model.LineSet
import com.db.williamchart.renderer.StringFormatRenderer
import com.db.williamchart.tooltip.Tooltip
import com.db.williamchart.util.GMStatisticUtil
import com.db.williamchart.util.KMNumbers
import com.db.williamchart.util.TooltipConfiguration
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.util.getResColor
import com.tokopedia.sellerhome.util.getResDrawable
import com.tokopedia.sellerhome.util.parseAsHtml
import com.tokopedia.sellerhome.view.model.LineGraphDataUiModel
import com.tokopedia.sellerhome.view.model.LineGraphWidgetUiModel
import kotlinx.android.synthetic.main.sah_line_graph_widget.view.*
import kotlinx.android.synthetic.main.sah_partial_common_widget_state_error.view.*
import kotlinx.android.synthetic.main.sah_partial_line_graph_widget_loading.view.*

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

class LineGraphViewHolder(
        view: View?,
        private val listener: Listener
) : AbstractViewHolder<LineGraphWidgetUiModel>(view) {

    companion object {
        val RES_LAYOUT: Int = R.layout.sah_line_graph_widget
    }

    override fun bind(element: LineGraphWidgetUiModel) = with(itemView) {

        observeState(element)
        listener.getLineGraphData()

        val data = element.data

        tvLineGraphTitle.text = element.title
        tvLineGraphValue.text = data?.header.orEmpty()
        tvLineGraphSubValue.text = data?.description.orEmpty().parseAsHtml()

        val colors = intArrayOf(context.getResColor(R.color.sah_green_light), Color.TRANSPARENT)
        lineGraphView.setGradientFillColors(colors)
    }

    private fun openAppLink(appLink: String) {
        if (appLink.isBlank()) return
        RouteManager.route(itemView.context, appLink)
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
                setupTooltip(element)
                onStateError(true)
            }
            else -> {
                onStateLoading(false)
                onStateError(false)
                setupTooltip(element)
                showViewComponent(true, element)
                showLineGraph(element)
            }
        }
    }

    private fun setupTooltip(element: LineGraphWidgetUiModel) = with(itemView) {
        element.tooltip?.let { tooltip ->
            if (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty()) {
                tvLineGraphTitle.setOnClickListener {
                    listener.onTooltipClicked(element.tooltip)
                }
                btnLineGraphInformation.visible()
                btnLineGraphInformation.setOnClickListener {
                    listener.onTooltipClicked(element.tooltip)
                }
            } else {
                btnLineGraphInformation.gone()
            }
        }
    }

    private fun onStateLoading(isShown: Boolean) = with(itemView) {
        shimmerLineGraphWidget.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    private fun onStateError(isShown: Boolean) = with(itemView) {
        ImageHandler.loadImageWithId(imgWidgetOnError, R.drawable.unify_globalerrors_connection)
        commonWidgetErrorState.visibility = if (isShown) View.VISIBLE else View.GONE
        tvLineGraphTitle.visibility = if (isShown) View.VISIBLE else View.INVISIBLE
        btnLineGraphInformation.visibility = if (isShown) View.VISIBLE else View.INVISIBLE
    }

    private fun showViewComponent(isShown: Boolean, element: LineGraphWidgetUiModel) = with(itemView) {
        val componentVisibility = if (isShown) View.VISIBLE else View.INVISIBLE
        tvLineGraphTitle.visibility = componentVisibility
        tvLineGraphValue.visibility = componentVisibility
        tvLineGraphSubValue.visibility = componentVisibility
        btnLineGraphMore.visibility = componentVisibility
        btnLineGraphNext.visibility = componentVisibility
        linearLineGraphView.visibility = componentVisibility
        btnLineGraphInformation.visibility = componentVisibility

        val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank() && isShown
        val ctaVisibility = if (isCtaVisible) View.VISIBLE else View.GONE
        btnLineGraphMore.visibility = ctaVisibility
        btnLineGraphNext.visibility = ctaVisibility
        btnLineGraphMore.text = element.ctaText

        if (isCtaVisible) {
            btnLineGraphMore.setOnClickListener {
                openAppLink(element.appLink)
            }
            btnLineGraphNext.setOnClickListener {
                openAppLink(element.appLink)
            }
        }
    }

    private fun showLineGraph(element: LineGraphWidgetUiModel) {
        val yValue: List<Int> = element.data?.list.orEmpty().map { it.yVal }
        val xLabel: List<String> = element.data?.list.orEmpty().map { it.xLabel }
        val lineGraphModel: BaseWilliamChartModel = GMStatisticUtil.getChartModel(xLabel, yValue)

        val lineGraphConfig: BaseWilliamChartConfig = getLineGraphConfig(lineGraphModel)
        with(lineGraphConfig) {
            setDotDrawable(itemView.context.getResDrawable(R.drawable.sah_oval_chart_dot))
            setLineSet(LineSet().apply {
                setDotsStrokeColor(itemView.context.getResColor(R.color.Green_G400))
            })
            buildChart(itemView.lineGraphView)
        }
    }

    private fun getLineGraphConfig(graphModel: BaseWilliamChartModel): BaseWilliamChartConfig {
        return Tools.getSellerHomeLineGraphWidgetConfig(
                itemView.lineGraphView, graphModel, getTooltip(), CustomTooltipConfiguration()
        )
    }

    private fun getTooltip(): Tooltip {
        return Tooltip(
                itemView.context,
                R.layout.sah_partial_line_graph_tooltip,
                R.id.tvTitle,
                R.id.tvValue,
                StringFormatRenderer { s ->
                    KMNumbers.formatSuffixNumbers(java.lang.Float.valueOf(s))
                }
        )
    }

    class CustomTooltipConfiguration : TooltipConfiguration {

        companion object {
            private const val DEFAULT_WIDTH = 56F
            private const val DEFAULT_HEIGHT = 32F
        }

        override fun width(): Int = Tools.fromDpToPx(DEFAULT_WIDTH).toInt()

        override fun height(): Int = Tools.fromDpToPx(DEFAULT_HEIGHT).toInt()
    }

    interface Listener : BaseViewHolderListener {
        fun getLineGraphData()
    }
}