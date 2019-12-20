package com.tokopedia.v2.home.base.adapterdelegate

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * [DelegateHandler]
 * is helper for adapter delegate pattern.
 * it will be handling adding new type from delegate
 * and add new item
 */
//abstract class DelegateHandler {
//
//    /**
//     * variable [delegateAdapters]
//     * is for hold list of type view
//     */
//    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
//
//    fun addDelegate(viewTypeDelegateAdapter: ViewTypeDelegateAdapter){
//        if(!delegateAdapters.containsValue(viewTypeDelegateAdapter)){
//            delegateAdapters.put(viewTypeDelegateAdapter.getViewType(), viewTypeDelegateAdapter)
//        }
//    }
//
//    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return delegateAdapters[viewType]?.onCreateViewHolder(parent) ?: throw IllegalStateException("viewType is defined, but does not exist on adapter delegates")
//    }
//
//    fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int){
//        delegateAdapters.get(position)?.onBindViewHolder(holder, items[position]) ?: throw IllegalStateException("viewType is defined, but does not exist on adapter delegates")
//    }
//
//}