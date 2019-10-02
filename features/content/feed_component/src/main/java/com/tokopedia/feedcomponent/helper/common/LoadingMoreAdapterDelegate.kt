package com.tokopedia.feedcomponent.helper.common

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingMoreViewHolder
import com.tokopedia.feedcomponent.helper.TypedAdapterDelegate

/**
 * Created by jegul on 2019-10-02.
 */
class LoadingMoreAdapterDelegate : TypedAdapterDelegate<LoadingMoreModel, LoadingMoreViewHolder>() {

    override fun isForViewType(item: Any, itemList: List<Any>, position: Int): Boolean {
        return item is LoadingMoreModel
    }

    override fun onBindViewHolder(item: LoadingMoreModel, holder: LoadingMoreViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup): LoadingMoreViewHolder {
        return LoadingMoreViewHolder(
                LayoutInflater.from(parent.context).inflate(LoadingMoreViewHolder.LAYOUT, parent, false)
        )
    }

    inner class LoadMoreViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(item: LoadingMoreModel) {}
    }
}