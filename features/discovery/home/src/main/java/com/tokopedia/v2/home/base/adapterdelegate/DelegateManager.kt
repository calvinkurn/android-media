package com.tokopedia.v2.home.base.adapterdelegate

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

class DelegateManager {
    private val types: SparseArrayCompat<ViewTypeDelegateAdapter> = SparseArrayCompat()

    fun addType(viewTypeDelegateAdapter: ViewTypeDelegateAdapter){
        if (!types.containsValue(viewTypeDelegateAdapter)) {
            val index = types.size()
            types.put(index, viewTypeDelegateAdapter)
        }
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return types[viewType]?.onCreateViewHolder(parent) ?: throw IllegalStateException("viewType is defined, but does not exist on adapter delegates")
    }

    fun getItemViewType(item: ModelViewType, position: Int): Int {
        return types.indexOfValue(getAdapterDelegate(item, position))
    }

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType, position: Int) {
        return getAdapterDelegate(item, position).onBindViewHolder(holder, item)
    }

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType, position: Int, payload: List<Any>) {
        return getAdapterDelegate(item, position).onBindViewHolder(holder, item, payload)
    }

    private fun getAdapterDelegate(item: ModelViewType, position: Int): ViewTypeDelegateAdapter {
        for (i in 0 until types.size()){
            val adapterDelegate = types[i]
            if(adapterDelegate?.isForViewType(item) == true) return adapterDelegate
        }
        throw IllegalArgumentException("No delegate is found for item: $item with type: ${item::class.java} on position: $position")
    }
}