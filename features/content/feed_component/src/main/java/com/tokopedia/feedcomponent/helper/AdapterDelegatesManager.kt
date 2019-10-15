package com.tokopedia.feedcomponent.helper

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by jegul on 2019-10-01.
 */
class AdapterDelegatesManager<T: Any> {

    private val typedAdapterDelegatesMap: MutableMap<Class<*>, Int> = LinkedHashMap()
    private val specialAdapterDelegates: MutableList<Int> = mutableListOf()

    private val adapterDelegates: SparseArrayCompat<AdapterDelegate<T>> = SparseArrayCompat()

    @Suppress("UNCHECKED_CAST")
    fun addDelegate(adapterDelegate: AdapterDelegate<T>): AdapterDelegatesManager<T> {
        val index = adapterDelegates.size()
        if (adapterDelegate is TypedAdapterDelegate<*, *, *>) {
            typedAdapterDelegatesMap[adapterDelegate.itemClass] = index
        } else {
            specialAdapterDelegates.add(index)
        }
        adapterDelegates.put(index, adapterDelegate)
        return this
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return adapterDelegates[viewType]?.onCreateViewHolder(parent, viewType) ?: throw IllegalStateException("viewType is defined, but does not exist on adapter delegates")
    }

    fun getItemViewType(itemList: List<T>, position: Int): Int {
        return adapterDelegates.indexOfValue(getAdapterDelegate(itemList, position))
    }

    @Suppress("UNCHECKED_CAST")
    fun onBindViewHolder(itemList: List<T>, position: Int, holder: RecyclerView.ViewHolder) {
        return getAdapterDelegate(itemList, position).onBindViewHolder(itemList, position, holder)
    }

    private fun getAdapterDelegate(itemList: List<T>, position: Int): AdapterDelegate<T> {
        val itemClass = itemList[position]::class.java
        if (typedAdapterDelegatesMap.containsKey(itemClass)) return adapterDelegates.get(typedAdapterDelegatesMap[itemClass]!!) ?: throw IllegalStateException("Index of adapter delegates found but does not exist in adapter delegate list")
        else {
            specialAdapterDelegates.forEach { delegateIndex ->
                if (adapterDelegates.get(delegateIndex)?.isForViewType(itemList, position) == true) return adapterDelegates.get(delegateIndex) as AdapterDelegate<T>
            }
            throw IllegalArgumentException("No delegate is found for item: ${itemList[position]} on position: $position")
        }
    }
}