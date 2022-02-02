package com.tokopedia.search.result.product.emptystate

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.common.data.Option

class EmptyStateSelectedFilterViewHolder(
    itemView: View,
    private val clickListener: EmptyStateListener
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = com.tokopedia.search.R.layout.search_product_empty_state_selected_filter_item
    }

    private val filterText: TextView = itemView.findViewById(com.tokopedia.search.R.id.filter_text)

    fun bind(option: Option) {
        filterText.text = option.name
        filterText.setOnClickListener {
            clickListener.onSelectedFilterRemoved(option.uniqueId)
        }
    }
}