package com.tokopedia.similarsearch.loadingmore

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.similarsearch.abstraction.BaseAdapterDelegate
import com.tokopedia.similarsearch.abstraction.BaseViewHolder
import com.tokopedia.similarsearch.utils.setFullSpanStaggeredGrid

internal class LoadingMoreAdapterDelegate: BaseAdapterDelegate<LoadingMoreModel, LoadingMoreViewHolder>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is LoadingMoreModel
    }

    override fun getViewHolderLayout(): Int {
        return LoadingMoreViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(inflatedView: View): RecyclerView.ViewHolder {
        return LoadingMoreViewHolder(inflatedView)
    }

    override fun onBindViewHolder(item: LoadingMoreModel, viewHolder: BaseViewHolder<LoadingMoreModel>) {
        viewHolder.setFullSpanStaggeredGrid()

        super.onBindViewHolder(item, viewHolder)
    }
}