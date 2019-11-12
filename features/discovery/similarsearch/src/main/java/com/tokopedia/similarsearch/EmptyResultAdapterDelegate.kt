package com.tokopedia.similarsearch

import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class EmptyResultAdapterDelegate: BaseAdapterDelegate<EmptyResultViewModel, EmptyResultViewHolder>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is EmptyResultViewModel
    }

    override fun getViewHolderLayout(): Int {
        return EmptyResultViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(inflatedView: View): RecyclerView.ViewHolder {
        return EmptyResultViewHolder(inflatedView)
    }

    override fun onBindViewHolder(item: EmptyResultViewModel, viewHolder: BaseViewHolder<EmptyResultViewModel>) {
        viewHolder.setFullSpanStaggeredGrid()

        return super.onBindViewHolder(item, viewHolder)
    }
}