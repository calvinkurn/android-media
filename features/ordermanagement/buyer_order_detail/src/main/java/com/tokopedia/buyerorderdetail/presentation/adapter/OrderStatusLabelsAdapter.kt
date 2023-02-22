package com.tokopedia.buyerorderdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.adapter.diffutil.OrderStatusLabelsDiffUtilCallback
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Label

class OrderStatusLabelsAdapter : RecyclerView.Adapter<OrderStatusLabelsAdapter.ViewHolder>() {

    private val labels: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_buyer_order_detail_order_status_label, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(labels.getOrNull(position))
    }

    fun setLabels(newLabels: List<String>) {
        val diffUtilCallback = OrderStatusLabelsDiffUtilCallback(labels.toMutableList(), newLabels)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        labels.clear()
        labels.addAll(newLabels)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(label: String?) {
            with(itemView as Label) {
                if (label.isNullOrBlank()) {
                    gone()
                } else {
                    setLabel(label)
                    show()
                }
            }
        }
    }
}
