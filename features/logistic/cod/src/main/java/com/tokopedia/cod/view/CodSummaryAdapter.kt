package com.tokopedia.cod.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.cod.R
import com.tokopedia.transactiondata.entity.response.cod.PriceSummary
import kotlinx.android.synthetic.main.item_summary.view.*
import kotlinx.android.synthetic.main.item_grand_total.view.*

/**
 * Created by fajarnuha on 23/12/18.
 */
class CodSummaryAdapter(val items: List<PriceSummary>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * The last item of PriceSummary data must be the Grand Total label
     * This is caused by api contract which provides as it is
     */
    override fun getItemViewType(position: Int) =
            if (position == items.size - 1) TotalViewHolder.TYPE else SummaryViewHolder.TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            TotalViewHolder.TYPE -> TotalViewHolder(view)
            else -> SummaryViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items.get(position).let {
            when (holder) {
                is SummaryViewHolder -> {
                    holder.bind(it)
                }
                is TotalViewHolder -> {
                    holder.bind(it)
                }
            }
        }
    }


    class SummaryViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        companion object {
            val TYPE = R.layout.item_summary
        }

        fun bind(summary: PriceSummary) {
            itemView.text_view_label.text = summary.label
            itemView.text_view_price.text = summary.priceFmt
            if (summary.labelInfo.isNotEmpty()) {
                itemView.text_view_label_info.visibility = View.VISIBLE
                itemView.text_view_label_info.text = summary.labelInfo
            }
        }
    }

    class TotalViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        companion object {
            val TYPE = R.layout.item_grand_total
        }

        fun bind(summary: PriceSummary) {
            itemView.text_view_total_label.text = summary.label
            itemView.text_view_total_price.text = summary.priceFmt
        }

    }
}