package com.tokopedia.similarsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

internal class DividerAdapterDelegate: BaseAdapterDelegate<DividerViewModel, DividerViewHolder>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is DividerViewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(DividerViewHolder.LAYOUT, parent, false)
        return DividerViewHolder(itemView)
    }
}