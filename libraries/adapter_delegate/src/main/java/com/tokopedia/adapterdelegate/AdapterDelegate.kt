package com.tokopedia.adapterdelegate

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by jegul on 2019-10-01.
 */
interface AdapterDelegate<T> {

    fun isForViewType(itemList: List<T>, position: Int, isFlexibleType: Boolean): Boolean

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    fun onBindViewHolder(itemList: List<T>, position: Int, holder: RecyclerView.ViewHolder)

    fun onBindViewHolder(itemList: List<T>, position: Int, payloads: Bundle, holder: RecyclerView.ViewHolder)
}