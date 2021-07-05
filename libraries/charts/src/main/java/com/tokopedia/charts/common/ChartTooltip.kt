package com.tokopedia.charts.common

import android.content.Context
import android.view.View
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.tokopedia.charts.model.LineChartEntry

/**
 * Created By @ilhamsuaib on 28/06/20
 */

open class ChartTooltip(
        private val context: Context?,
        private val layoutResource: Int
) {

    internal val markerView: MarkerView
    private var refreshContent: ((view: View, data: Any?, x: Float, y: Float) -> Unit)? = null

    init {
        markerView = object : MarkerView(context, layoutResource) {
            override fun refreshContent(e: Entry?, highlight: Highlight?) {
                e?.let {
                    refreshContent?.invoke(this, it.data, it.x, it.y)
                }
                super.refreshContent(e, highlight)
            }

            override fun getOffset(): MPPointF {
                return MPPointF(-(width / 2f), -height.toFloat())
            }
        }
    }

    fun setOnDisplayContent(callback: (view: View, data: Any?, x: Float, y: Float) -> Unit): ChartTooltip {
        this.refreshContent = callback
        return this
    }
}