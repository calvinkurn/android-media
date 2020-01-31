package com.tokopedia.sellerhome.view.viewholder

import android.graphics.Color
import android.view.View
import com.db.williamchart.Tools
import com.db.williamchart.base.BaseWilliamChartConfig
import com.db.williamchart.base.BaseWilliamChartModel
import com.db.williamchart.model.LineSet
import com.db.williamchart.util.GMStatisticUtil
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.util.getResColor
import com.tokopedia.sellerhome.util.getResDrawable
import com.tokopedia.sellerhome.util.parseAsHtml
import com.tokopedia.sellerhome.util.toast
import com.tokopedia.sellerhome.view.model.LineGraphDataUiModel
import com.tokopedia.sellerhome.view.model.LineGraphWidgetUiModel
import kotlinx.android.synthetic.main.sah_line_graph_widget.view.*
import kotlinx.android.synthetic.main.sah_partial_line_graph_widget_error.view.*
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

        tvLineGraphTitle.text = element.title
        tvLineGraphValue.text = element.data?.header.orEmpty()
        tvLineGraphSubValue.text = element.data?.description.orEmpty().parseAsHtml()
        btnLineGraphMore.setOnClickListener {
            context.toast("Selengkapnya")
        }
        btnLineGraphNext.setOnClickListener {
            context.toast("Selengkapnya")
        }
        tvLineGraphTitle.setOnClickListener {
            context.toast("Information")
        }
        btnLineGraphInformation.setOnClickListener {
            context.toast("Information")
        }

        val colors = intArrayOf(context.getResColor(R.color.sah_green_light), Color.TRANSPARENT)
        lineGraphView.setGradientFillColors(colors)
    }

    /**
     * Loading State -> when data is null. By default when adapter created
     * it will pass null data.
     * Error State -> when error field have a string
     * Success State -> when data not null &
     * */

    private fun observeState(element: LineGraphWidgetUiModel) {
        val data: LineGraphDataUiModel? = element.data
        when {
            null == data -> {
                showViewComponent(false)
                onStateError(false)
                onStateLoading(true)
            }
            data.error.isNotBlank() -> {
                onStateLoading(false)
                showViewComponent(false)
                onStateError(true)
            }
            else -> {
                onStateLoading(false)
                onStateError(false)
                showViewComponent(true)
                showLineGraph(element)
            }
        }
    }

    private fun onStateLoading(isShown: Boolean) = with(itemView) {
        shimmerLineGraphWidget.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    private fun onStateError(isShown: Boolean) = with(itemView) {
        ImageHandler.loadImageWithId(imgLineGraphError, R.drawable.unify_globalerrors_connection)
        layoutLineGraphErrorState.visibility = if (isShown) View.VISIBLE else View.GONE
        tvLineGraphTitle.visibility = if (isShown) View.VISIBLE else View.INVISIBLE
        btnLineGraphInformation.visibility = if (isShown) View.VISIBLE else View.INVISIBLE
    }

    private fun showViewComponent(isShown: Boolean) = with(itemView) {
        val componentVisibility = if (isShown) View.VISIBLE else View.INVISIBLE
        tvLineGraphTitle.visibility = componentVisibility
        tvLineGraphValue.visibility = componentVisibility
        tvLineGraphSubValue.visibility = componentVisibility
        btnLineGraphMore.visibility = componentVisibility
        btnLineGraphNext.visibility = componentVisibility
        btnLineGraphInformation.visibility = componentVisibility
        linearLineGraphView.visibility = componentVisibility
    }

    private fun showLineGraph(element: LineGraphWidgetUiModel) {
        val yValue: List<Int> = element.data?.list.orEmpty().map { it.yVal }
        val xLabel: List<String> = element.data?.list.orEmpty().map { it.xLabel }
        val lineGraphModel: BaseWilliamChartModel = GMStatisticUtil.getChartModel(xLabel, yValue)
        val lineGraphConfig: BaseWilliamChartConfig = Tools.getSellerHomeLineGraphWidgetConfig(itemView.lineGraphView, lineGraphModel)
        lineGraphConfig.setDotDrawable(itemView.context.getResDrawable(R.drawable.sah_oval_chart_dot))
        lineGraphConfig.setLineSet(LineSet().apply {
            setDotsStrokeColor(itemView.context.getResColor(R.color.Green_G400))
        })
        lineGraphConfig.buildChart(itemView.lineGraphView)
    }

    interface Listener {
        fun getLineGraphData()
    }
}