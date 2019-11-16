package com.tokopedia.similarsearch

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

internal interface AdapterDelegate<T> {

    fun isForViewType(items: List<T>, position: Int): Boolean

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(items: List<T>, viewHolder: RecyclerView.ViewHolder, position: Int)

    fun onBindViewHolder(items: List<T>, viewHolder: RecyclerView.ViewHolder, position: Int, payload: List<Any>)
}