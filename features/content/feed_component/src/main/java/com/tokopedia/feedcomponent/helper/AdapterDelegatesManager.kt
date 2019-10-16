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

    /**
     * Adding delegate to the list of delegates
     * If the adapter delegate to be added is of type [TypedAdapterDelegate], it will be put in a Map
     * Other type will be added to a regular list
     * @param adapterDelegate - adapter delegate to be added
     * @return [AdapterDelegatesManager] to support chaining
     */
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

    fun onBindViewHolder(itemList: List<T>, position: Int, holder: RecyclerView.ViewHolder) {
        return getAdapterDelegate(itemList, position).onBindViewHolder(itemList, position, holder)
    }

    private fun getAdapterDelegate(itemList: List<T>, position: Int): AdapterDelegate<T> {
        val item = itemList[position]
        val itemClass = item::class.java
        if (typedAdapterDelegatesMap.containsKey(itemClass)) return adapterDelegates[typedAdapterDelegatesMap[itemClass]!!] ?: throw IllegalStateException("Index of adapter delegates found but does not exist in adapter delegate list")
        else {
            specialAdapterDelegates.forEach { delegateIndex ->
                val adapterDelegate = adapterDelegates[delegateIndex]
                if (adapterDelegate?.isForViewType(itemList, position) == true) return adapterDelegate
            }
            throw IllegalArgumentException("No delegate is found for item: $item with type: $itemClass on position: $position")
        }
    }
}