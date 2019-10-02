package com.tokopedia.feedcomponent.helper

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by jegul on 2019-10-01.
 */
abstract class TypedAdapterDelegate<T: ST, ST: Any, VH : RecyclerView.ViewHolder> : AdapterDelegate<Any> {

    abstract fun isForViewType(item: ST, itemList: List<ST>, position: Int): Boolean

    abstract fun onBindViewHolder(item: T, holder: VH)

    abstract fun onCreateViewHolder(parent: ViewGroup): VH

    @Suppress("UNCHECKED_CAST")
    override fun isForViewType(itemList: List<Any>, position: Int): Boolean {
        return isForViewType(itemList[position] as ST, itemList as List<ST>, position)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(itemList: List<Any>, position: Int, holder: RecyclerView.ViewHolder) {
        onBindViewHolder(itemList[position] as T, holder as VH)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onCreateViewHolder(parent)
    }
}