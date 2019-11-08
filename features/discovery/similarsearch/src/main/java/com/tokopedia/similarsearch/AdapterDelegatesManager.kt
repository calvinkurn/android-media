package com.tokopedia.similarsearch

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

internal class AdapterDelegatesManager {

    private val adapterDelegateList = mutableListOf<AdapterDelegate<Any>>()

    fun addDelegate(adapterDelegate: AdapterDelegate<Any>): AdapterDelegatesManager {
        adapterDelegateList.add(adapterDelegate)
        return this
    }

    fun getItemViewType(items: List<Any>, position: Int): Int {
        adapterDelegateList.forEachIndexed { index, it ->
            if (it.isForViewType(items, position)) return index
        }

        return -1
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return adapterDelegateList[viewType].onCreateViewHolder(parent)
    }

    fun onBindViewHolder(item: List<Any>, viewHolder: RecyclerView.ViewHolder, position:Int) {
        return adapterDelegateList[viewHolder.itemViewType].onBindViewHolder(item, viewHolder, position)
    }
}