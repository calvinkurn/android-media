package com.tokopedia.adapterdelegate

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by jegul on 2019-10-16
 */
abstract class BaseAdapterDelegate<T: ST, ST: Any, VH : RecyclerView.ViewHolder>(@LayoutRes private val layoutRes: Int) : AdapterDelegate<ST> {

    abstract fun onBindViewHolder(item: T, holder: VH)

    open fun onBindViewHolderWithPayloads(item: T, holder: VH, payloads: Bundle) {
        onBindViewHolder(item, holder)
    }

    abstract fun onCreateViewHolder(parent: ViewGroup, basicView: View): VH

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(itemList: List<ST>, position: Int, holder: RecyclerView.ViewHolder) {
        onBindViewHolder(itemList[position] as T, holder as VH)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(itemList: List<ST>, position: Int, payloads: Bundle, holder: RecyclerView.ViewHolder) {
        onBindViewHolderWithPayloads(itemList[position] as T, holder as VH, payloads)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onCreateViewHolder(parent, getView(parent, layoutRes))
    }
}