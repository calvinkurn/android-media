package com.tokopedia.sellerhome.view.viewholder

import android.graphics.Color
import android.os.Handler
import android.view.View
import com.db.williamchart.Tools
import com.db.williamchart.base.BaseWilliamChartConfig
import com.db.williamchart.base.BaseWilliamChartModel
import com.db.williamchart.model.LineSet
import com.db.williamchart.util.GMStatisticUtil
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.util.getResColor
import com.tokopedia.sellerhome.util.getResDrawable
import com.tokopedia.sellerhome.util.parseAsHtml
import com.tokopedia.sellerhome.util.toast
import com.tokopedia.sellerhome.view.model.LineGraphWidgetUiModel
import kotlinx.android.synthetic.main.sah_line_graph_widget.view.*
import kotlinx.android.synthetic.main.sah_partial_line_graph_widget_error.view.*
import kotlinx.android.synthetic.main.sah_partial_line_graph_widget_loading.view.*

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

class LineGraphViewHolder(view: View?) : AbstractViewHolder<LineGraphWidgetUiModel>(view) {

    companion object {
        val RES_LAYOUT: Int = R.layout.sah_line_graph_widget
    }

    override fun bind(element: LineGraphWidgetUiModel) = with(itemView) {
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

        Handler().postDelayed({
            showShimmering(false)
            showOnErrorState(true)
        }, 5000)

        Handler().postDelayed({
            showOnErrorState(false)
            showLineGraph(element)
        }, 10000)

        showViewComponent(false)

        showOnErrorState(false)
        showShimmering(true)
    }

    private fun showShimmering(isShown: Boolean) = with(itemView) {
        shimmerLineGraphWidget.visibility = if (isShown) View.VISIBLE else View.GONE
        showViewComponent(!isShown)
    }

    private fun showOnErrorState(isShown: Boolean) = with(itemView) {
        layoutLineGraphErrorState.visibility = if (isShown) View.VISIBLE else View.GONE
        showViewComponent(!isShown)
        tvLineGraphTitle.visibility = View.VISIBLE
        btnLineGraphInformation.visibility = View.VISIBLE
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
        val lineGraphModel: BaseWilliamChartModel = GMStatisticUtil.getChartModel(getDateGraph(element), getData(element))
        val lineGraphConfig: BaseWilliamChartConfig = Tools.getSellerHomeLineGraphWidgetConfig(itemView.lineGraphView, lineGraphModel)
        lineGraphConfig.setDotDrawable(itemView.context.getResDrawable(R.drawable.sah_oval_chart_dot))
        lineGraphConfig.setLineSet(LineSet().apply {
            setDotsStrokeColor(itemView.context.getResColor(R.color.Green_G400))
        })
        lineGraphConfig.buildChart(itemView.lineGraphView)
    }

    private fun getData(element: LineGraphWidgetUiModel): List<Int>? {
        return element.data?.list.orEmpty().map { it.yVal.toInt() }
    }

    private fun getDateGraph(element: LineGraphWidgetUiModel): List<String>? {
        return element.data?.list.orEmpty().map { it.xLabel }
    }
}