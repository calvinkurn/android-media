package com.tokopedia.charts.view.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.charts.R
import com.tokopedia.charts.model.PieChartEntry
import kotlinx.android.synthetic.main.item_pie_chart_legend.view.*

/**
 * Created By @ilhamsuaib on 07/07/20
 */

class PieChartLegendAdapter : RecyclerView.Adapter<PieChartLegendAdapter.ViewHolder>() {

    private var items = emptyList<PieChartEntry>()

    fun setLegends(items: List<PieChartEntry>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_pie_chart_legend, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: PieChartEntry) = with(itemView) {
            vPieChartLegendDot.background = getShapeDrawable(item.hexColor)
            tvPieChartLegend.text = item.legend
            tvPieChartLegendValue.text = item.valueFmt
        }

        private fun getShapeDrawable(hexColor: String): Drawable {
            val dp8: Float = itemView.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)
            val radius: FloatArray = floatArrayOf(dp8, dp8, dp8, dp8, dp8, dp8, dp8, dp8)
            val shape = ShapeDrawable(RoundRectShape(radius, null, null))
            shape.paint.color = Color.parseColor(hexColor)
            return shape
        }
    }
}