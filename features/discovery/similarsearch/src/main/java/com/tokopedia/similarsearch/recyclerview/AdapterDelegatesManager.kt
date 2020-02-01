package com.tokopedia.similarsearch.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.similarsearch.abstraction.AdapterDelegate

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

    fun onBindViewHolder(items: List<Any>, viewHolder: RecyclerView.ViewHolder, position:Int) {
        return adapterDelegateList[viewHolder.itemViewType].onBindViewHolder(items, viewHolder, position)
    }

    fun onBindViewHolder(items: List<Any>, viewHolder: RecyclerView.ViewHolder, position: Int, payload: List<Any>) {
        return adapterDelegateList[viewHolder.itemViewType].onBindViewHolder(items, viewHolder, position, payload)
    }
}