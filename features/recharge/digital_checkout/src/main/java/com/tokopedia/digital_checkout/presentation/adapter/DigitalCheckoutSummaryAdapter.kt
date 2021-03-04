package com.tokopedia.digital_checkout.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_checkout.R
import kotlinx.android.synthetic.main.item_digital_checkout_summary_detail.view.*

class DigitalCheckoutSummaryAdapter: RecyclerView.Adapter<DigitalCheckoutSummaryAdapter.DigitalCheckoutSummaryViewHolder>() {

    private var summaries: MutableList<DummyItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigitalCheckoutSummaryViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(DigitalCheckoutSummaryViewHolder.LAYOUT, parent, false)
        return DigitalCheckoutSummaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DigitalCheckoutSummaryViewHolder, position: Int) {
        holder.bind(summaries[position])
    }

    override fun getItemCount(): Int = summaries.size

    fun addItem(item: DummyItem, position: Int) {
        val insertPosition = if (position < itemCount) position else itemCount
        summaries.add(insertPosition, item)
        notifyItemInserted(itemCount - 1)
    }

    fun removeItem(title: String) {
        val item: DummyItem = summaries.firstOrNull { it.title == title } ?: return
        val deletePosition = summaries.indexOf(item)
        summaries.removeAt(deletePosition)
        notifyItemRemoved(deletePosition)
    }

    fun getAllItems(): List<DummyItem> = summaries

    class DigitalCheckoutSummaryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        companion object {
            val LAYOUT = R.layout.item_digital_checkout_summary_detail
        }
        fun bind(item: DummyItem) {
            with(itemView) {
                tvCheckoutSummaryDetailLabel.text = item.title
                tvCheckoutSummaryDetailValue.text = item.priceAmount.toString()
            }
        }
    }

    // TODO : replace this with actual class
    data class DummyItem (
            val title: String,
            val priceAmount: Int
    )
}