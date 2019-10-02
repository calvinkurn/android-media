package com.tokopedia.feedcomponent.helper

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by jegul on 2019-10-01.
 */
class AdapterDelegatesManager<T> {

    private val adapterDelegates: MutableList<AdapterDelegate<Any>> = mutableListOf()

    @Suppress("UNCHECKED_CAST")
    fun addDelegate(adapterDelegate: AdapterDelegate<T>): AdapterDelegatesManager<T> {
        adapterDelegates.add(adapterDelegate as AdapterDelegate<Any>)
        return this
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return adapterDelegates[viewType].onCreateViewHolder(parent, viewType)
    }

    @Suppress("UNCHECKED_CAST")
    fun getItemViewType(itemList: List<T>, position: Int): Int {
        adapterDelegates.forEachIndexed { index, delegate ->
            if (delegate.isForViewType(itemList as List<Any>, position)) return index
        }
        throw IllegalArgumentException("No delegate is found for item: ${itemList[position]} on position: $position")
    }

    @Suppress("UNCHECKED_CAST")
    fun onBindViewHolder(itemList: List<T>, position: Int, holder: RecyclerView.ViewHolder) {
        return adapterDelegates[holder.itemViewType].onBindViewHolder(itemList as List<Any>, position, holder)
    }
}