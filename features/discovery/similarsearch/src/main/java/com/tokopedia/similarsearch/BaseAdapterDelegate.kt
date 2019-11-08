package com.tokopedia.similarsearch

import androidx.recyclerview.widget.RecyclerView

internal abstract class BaseAdapterDelegate<T, VH: BaseViewHolder<T>>: AdapterDelegate<Any> {

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(items: List<Any>, viewHolder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(items[position] as T, viewHolder as VH)
    }

    private fun onBindViewHolder(item: T, viewHolder: BaseViewHolder<T>) {
        viewHolder.bind(item)
    }
}
