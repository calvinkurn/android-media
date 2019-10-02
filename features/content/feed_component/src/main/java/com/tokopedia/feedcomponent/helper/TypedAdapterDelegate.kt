package com.tokopedia.feedcomponent.helper

import android.support.v7.widget.RecyclerView

/**
 * Created by jegul on 2019-10-01.
 */
abstract class TypedAdapterDelegate<T: ST, ST: Any, VH : RecyclerView.ViewHolder> : AdapterDelegate<ST> {

    abstract fun isForViewType(item: ST, itemList: List<ST>, position: Int): Boolean

    abstract fun onBindViewHolder(item: T, holder: VH)

    override fun isForViewType(itemList: List<ST>, position: Int): Boolean {
        return isForViewType(itemList[position], itemList, position)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(itemList: List<ST>, position: Int, holder: RecyclerView.ViewHolder) {
        onBindViewHolder(itemList[position] as T, holder as VH)
    }
}