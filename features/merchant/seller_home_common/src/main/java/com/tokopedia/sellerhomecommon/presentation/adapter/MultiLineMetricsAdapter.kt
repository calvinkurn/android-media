package com.tokopedia.sellerhomecommon.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineMetricUiModel
import kotlinx.android.synthetic.main.shc_item_multi_line_metric.view.*

/**
 * Created By @ilhamsuaib on 27/10/20
 */

class MultiLineMetricsAdapter(
        private val listener: MetricsListener
) : RecyclerView.Adapter<MultiLineMetricsAdapter.ViewHolder>() {

    var items: List<MultiLineMetricUiModel> = emptyList()
        private set
    private var recyclerViewWidth = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.shc_item_multi_line_metric, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, recyclerViewWidth)
    }

    fun setItems(items: List<MultiLineMetricUiModel>) {
        this.items = items
    }

    fun setRecyclerViewWidth(width: Int) {
        recyclerViewWidth = width
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: MultiLineMetricUiModel, recyclerViewWidth: Int) {
            val summary = item.summary

            with(itemView) {
                val cardWidth = getCardWidth(recyclerViewWidth)
                containerShcCardMetric.layoutParams.width = cardWidth

                tvShcMetricsTitle.text = summary.title
                tvShcMetricsValue.text = summary.valueFmt.parseAsHtml()
                tvShcMetricsSubValue.text = summary.description.parseAsHtml()

                if (summary.lineColor.isNotBlank() && item.isSelected) {
                    val metricColor = Color.parseColor(summary.lineColor)
                    viewShcMetricsColor.setBackgroundColor(metricColor)
                } else {
                    viewShcMetricsColor.setBackgroundColor(Color.TRANSPARENT)
                }

                setOnClickListener {
                    listener.onItemClickListener(item, adapterPosition)
                }

                val isMetricError = item.errorMsg.isNotEmpty() || item.isError ||
                        item.linePeriod.currentPeriod.isEmpty()
                if (isMetricError) {
                    tvShcMetricsValue.text = context.getString(R.string.shc_failed_to_load)
                    tvShcMetricsSubValue.text = ""
                }
            }
        }

        private fun getCardWidth(recyclerViewWidth: Int): Int {
            if (recyclerViewWidth == 0) {
                val defaultWidth = itemView.context.resources.getDimension(R.dimen.shc_dimen_150dp)
                return defaultWidth.toInt()
            }

            val cardSpace = itemView.context.dpToPx(16).toInt()
            val rvLeftAndRightMargin = itemView.context.dpToPx(24).toInt()
            return (recyclerViewWidth / 2) - (cardSpace + rvLeftAndRightMargin)
        }
    }

    interface MetricsListener {
        fun onItemClickListener(metric: MultiLineMetricUiModel, position: Int)
    }
}