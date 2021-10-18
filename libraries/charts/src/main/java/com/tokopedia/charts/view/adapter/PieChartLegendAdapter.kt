package com.tokopedia.charts.view.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.charts.databinding.ItemPieChartLegendBinding
import com.tokopedia.charts.model.PieChartEntry

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
        val binding = ItemPieChartLegendBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ItemPieChartLegendBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PieChartEntry) = with(binding) {
            vPieChartLegendDot.background = getShapeDrawable(item.hexColor)
            tvPieChartLegend.text = item.legend
            tvPieChartLegendValue.text = item.valueFmt
        }

        private fun getShapeDrawable(hexColor: String): Drawable {
            val dp8: Float = itemView.context.resources.getDimension(
                com.tokopedia.unifyprinciples.R.dimen.layout_lvl1
            )
            val radius: FloatArray = floatArrayOf(dp8, dp8, dp8, dp8, dp8, dp8, dp8, dp8)
            val shape = ShapeDrawable(RoundRectShape(radius, null, null))
            shape.paint.color = Color.parseColor(hexColor)
            return shape
        }
    }
}