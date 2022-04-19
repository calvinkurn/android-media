package com.tokopedia.csat_rating.quickfilter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.csat_rating.R

open class QuickSingleFilterAdapter(protected var actionListener: QuickSingleFilterListener?) : BaseQuickSingleFilterAdapter<ItemFilterViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFilterViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.csat_rating_item_quick_filter_view, parent, false)
        return ItemFilterViewHolder(view, actionListener)
    }

    override fun onBindViewHolder(holder: ItemFilterViewHolder?, position: Int) {
        holder?.renderItemViewHolder(filterList[position])
    }
}