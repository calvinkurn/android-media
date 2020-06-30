package com.tokopedia.buyerorder.unifiedhistory.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import kotlinx.android.synthetic.main.quick_filter_item_uoh.view.*

/**
 * Created by fwidjaja on 30/06/20.
 */
class UohQuickFilterItemAdapter: RecyclerView.Adapter<UohQuickFilterItemAdapter.ViewHolder>() {
    private var listChips = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.quick_filter_item_uoh, parent, false))
    }

    override fun getItemCount(): Int {
        return listChips.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder.itemView.chips_filter.chipText = ""
            holder.itemView.chips_filter.chipImageResource = holder.itemView.resources.getDrawable(R.drawable.ic_uoh_filter_close)
            holder.itemView.chips_filter.setOnClickListener {
                // reset the filter
            }
        } else {
            holder.itemView.chips_filter.chipText = listChips[position-1]
            holder.itemView.chips_filter.chip_right_icon.setImageResource(R.drawable.ic_uoh_filter_chevron_down)
            holder.itemView.chips_filter.setOnClickListener {
                // show bottomsheet!
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}