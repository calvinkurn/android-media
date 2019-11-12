package com.tokopedia.similarsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

internal class EmptyResultAdapterDelegate: BaseAdapterDelegate<EmptyResultViewModel, EmptyResultViewHolder>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is EmptyResultViewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(EmptyResultViewHolder.LAYOUT, parent, false)
        return EmptyResultViewHolder(view)
    }
}