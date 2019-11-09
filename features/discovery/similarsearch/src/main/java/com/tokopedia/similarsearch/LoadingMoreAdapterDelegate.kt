package com.tokopedia.similarsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel

internal class LoadingMoreAdapterDelegate: BaseAdapterDelegate<LoadingMoreModel, LoadingMoreViewHolder>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is LoadingMoreModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(LoadingMoreViewHolder.LAYOUT, parent, false)
        return LoadingMoreViewHolder(itemView)
    }

    override fun onBindViewHolder(item: LoadingMoreModel, viewHolder: BaseViewHolder<LoadingMoreModel>) {
        viewHolder.setFullSpanStaggeredGrid()

        super.onBindViewHolder(item, viewHolder)
    }
}