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
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.sellerhome.R
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
        tvLineGraphValue.text = "Rp200.000"
        tvLineGraphSubValue.text = "<span style=color:#03ac0e;><b>+25%</b></span>".parseAsHtml()
        btnLineGraphMore.setOnClickListener {
            context.toast("Selengkapnya")
        }
        btnLineGraphInformation.setOnClickListener {
            context.toast("Information")
        }

        val colors = intArrayOf(Color.parseColor("#66E76B"), Color.TRANSPARENT)
        lineGraphView.setGradientFillColors(colors)

        Handler().postDelayed({
            showShimmering(false)
            showOnErrorState(true)
        }, 5000)

        Handler().postDelayed({
            showOnErrorState(false)
            showLineGraph()
        }, 12000)

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
        ImageHandler.loadImageWithId(imgLineGraphError, R.drawable.img_sah_error)
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
        btnLineGraphInformation.visibility = componentVisibility
        linearLineGraphView.visibility = componentVisibility
    }

    private fun showLineGraph() {
        val lineGraphModel: BaseWilliamChartModel = GMStatisticUtil.getChartModel(getDateGraph(), getData())
        val lineGraphConfig: BaseWilliamChartConfig = Tools.getSellerHomeLineGraphWidgetConfig(itemView.lineGraphView, lineGraphModel)
        lineGraphConfig.setDotDrawable(itemView.context.getResDrawable(R.drawable.sah_oval_chart_dot))
        lineGraphConfig.setLineSet(LineSet().apply {
            setDotsStrokeColor(Color.parseColor("#4fd15a"))
        })
        lineGraphConfig.buildChart(itemView.lineGraphView)
    }

    private fun getData(): List<Int>? {
        val integers = mutableListOf<Int>()
        for (i in 0..6) {
            when {
                i == 0 -> integers.add(800)
                i % 2 == 0 -> integers.add(i * 100)
                else -> integers.add(i * 300)
            }
        }
        return integers
    }

    private fun getDateGraph(): List<String>? {
        val days = mutableListOf<String>()
        days.add("Sun")
        days.add("Mon")
        days.add("Tue")
        days.add("Wed")
        days.add("Thu")
        days.add("Fri")
        days.add("Sat")
        return days
    }
}