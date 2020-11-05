package com.tokopedia.sellerhomecommon.presentation.adapter

import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.shc_item_multi_line_metric, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    fun setItems(items: List<MultiLineMetricUiModel>) {
        this.items = items
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: MultiLineMetricUiModel) {
            val summary = item.summary

            with(itemView) {
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
    }

    interface MetricsListener {
        fun onItemClickListener(metric: MultiLineMetricUiModel, position: Int)
    }
}