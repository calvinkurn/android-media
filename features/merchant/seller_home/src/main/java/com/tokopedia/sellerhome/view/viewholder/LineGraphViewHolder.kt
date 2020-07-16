package com.tokopedia.sellerhome.view.viewholder

import android.graphics.Color
import android.view.View
import com.db.williamchart.Tools
import com.db.williamchart.base.BaseWilliamChartConfig
import com.db.williamchart.base.BaseWilliamChartModel
import com.db.williamchart.renderer.StringFormatRenderer
import com.db.williamchart.tooltip.Tooltip
import com.db.williamchart.util.GMStatisticUtil
import com.db.williamchart.util.KMNumbers
import com.db.williamchart.util.TooltipConfiguration
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.analytic.SellerHomeTracking
import com.tokopedia.sellerhome.view.model.LineGraphDataUiModel
import com.tokopedia.sellerhome.view.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhome.view.widget.linegraph.SellerHomeDataSetConfig
import com.tokopedia.sellerhome.view.widget.linegraph.SellerHomeLineGraphConfig
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
        setIsRecyclable(false)
        observeState(element)

        val data = element.data

        tvLineGraphTitle.text = element.title
        tvLineGraphValue.text = data?.header.orEmpty()
        tvLineGraphSubValue.text = data?.description.orEmpty().parseAsHtml()

        val colors = intArrayOf(context.getResColor(R.color.sah_green_light), Color.TRANSPARENT)
        lineGraphView.setGradientFillColors(colors)

        if (null != data)
            setupTooltip(element)
    }

    private fun openAppLink(appLink: String, dataKey: String, value: String) {
        if (RouteManager.route(itemView.context, appLink)) {
            SellerHomeTracking.sendClickLineGraphEvent(dataKey, value)
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
                SellerHomeTracking.sendImpressionLineGraphEvent(element.dataKey, element.data?.header.orEmpty())
            }
        }
    }

    private fun showLineGraph(element: LineGraphWidgetUiModel) {
        val yValue: List<Int> = element.data?.list.orEmpty().map { it.yVal }
        val xLabel: List<String> = element.data?.list.orEmpty().map { it.xLabel }
        val customValues: List<String> = element.data?.list.orEmpty().map { it.yLabel }
        val lineGraphModel: BaseWilliamChartModel = GMStatisticUtil.getChartModel(xLabel, yValue, customValues)

        val lineGraphConfig: BaseWilliamChartConfig = getLineGraphConfig(lineGraphModel)
        with(lineGraphConfig) {
            setDotDrawable(itemView.context.getResDrawable(R.drawable.sah_oval_chart_dot))
            setDotsStrokeColor(itemView.context.getResColor(R.color.Green_G400))
            buildChart(itemView.lineGraphView)
        }
    }

    private fun getLineGraphConfig(graphModel: BaseWilliamChartModel): BaseWilliamChartConfig {
        val chartMarginTop = (itemView.lineGraphView.layoutParams.height / 3) - 20
        val lineGraphConfig = SellerHomeLineGraphConfig().apply {
            setMarginRight(15)
            setMarginTop(chartMarginTop)
        }
        return Tools.getCommonWilliamChartConfig(itemView.lineGraphView, graphModel,
                SellerHomeDataSetConfig(), getTooltip(), CustomTooltipConfiguration(), lineGraphConfig)
    }

    private fun getTooltip(): Tooltip {
        return Tooltip(
                itemView.context,
                R.layout.sah_partial_line_graph_tooltip,
                R.id.tvTitle,
                R.id.tvValue,
                StringFormatRenderer { s ->
                    return@StringFormatRenderer try {
                        KMNumbers.formatSuffixNumbers(java.lang.Float.valueOf(s))
                    } catch (e: NumberFormatException) {
                        s
                    }
                }
        )
    }

    class CustomTooltipConfiguration : TooltipConfiguration {

        companion object {
            private const val DEFAULT_WIDTH = 68F
            private const val DEFAULT_HEIGHT = 30F
        }

        override fun width(): Int = Tools.fromDpToPx(DEFAULT_WIDTH).toInt()

        override fun height(): Int = Tools.fromDpToPx(DEFAULT_HEIGHT).toInt()
    }

    interface Listener : BaseViewHolderListener {
        fun getLineGraphData()
    }
}