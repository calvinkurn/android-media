package com.tokopedia.similarsearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

internal abstract class BaseAdapterDelegate<T, VH: BaseViewHolder<T>>: AdapterDelegate<Any> {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(getViewHolderLayout(), parent, false)
        return onCreateViewHolder(view)
    }

    @LayoutRes
    protected abstract fun getViewHolderLayout(): Int

    protected abstract fun onCreateViewHolder(inflatedView: View): RecyclerView.ViewHolder

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(items: List<Any>, viewHolder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(items[position] as T, viewHolder as VH)
    }

    protected open fun onBindViewHolder(item: T, viewHolder: BaseViewHolder<T>) {
        viewHolder.bind(item)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(items: List<Any>, viewHolder: RecyclerView.ViewHolder, position: Int, payload: List<Any>) {
        onBindViewHolder(items[position] as T, viewHolder as VH, payload)
    }

    protected open fun onBindViewHolder(item: T, viewHolder: BaseViewHolder<T>, payload: List<Any>) {
        viewHolder.bind(payload)
    }
}
