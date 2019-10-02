package com.tokopedia.feedcomponent.helper

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by jegul on 2019-10-01.
 */
class AdapterDelegatesManager<T> {

    private val adapterDelegates: MutableList<AdapterDelegate<T>> = mutableListOf()

    fun addDelegate(adapterDelegate: AdapterDelegate<T>): AdapterDelegatesManager<T> {
        adapterDelegates.add(adapterDelegate)
        return this
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return adapterDelegates[viewType].onCreateViewHolder(parent)
    }

    fun getItemViewType(itemList: List<T>, position: Int): Int {
        adapterDelegates.forEachIndexed { index, delegate ->
            if (delegate.isForViewType(itemList, position)) return index
        }
        throw IllegalArgumentException("No delegate is found for item: ${itemList[position]} on position: $position")
    }

    fun onBindViewHolder(itemList: List<T>, position: Int, holder: RecyclerView.ViewHolder) {
        return adapterDelegates[holder.itemViewType].onBindViewHolder(itemList, position, holder)
    }
}