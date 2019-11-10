package com.tokopedia.v2.home.base.adapterdelegate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface ViewTypeDelegateAdapter {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType)
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType, payload: List<Any>)
    fun isForViewType(item: ModelViewType): Boolean
}