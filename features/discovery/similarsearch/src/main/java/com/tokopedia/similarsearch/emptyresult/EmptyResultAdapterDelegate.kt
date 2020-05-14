package com.tokopedia.similarsearch.emptyresult

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.similarsearch.abstraction.BaseAdapterDelegate
import com.tokopedia.similarsearch.abstraction.BaseViewHolder
import com.tokopedia.similarsearch.utils.setFullSpanStaggeredGrid

internal class EmptyResultAdapterDelegate(
        private val emptyResultListener: EmptyResultListener
): BaseAdapterDelegate<EmptyResultViewModel, EmptyResultViewHolder>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is EmptyResultViewModel
    }

    override fun getViewHolderLayout(): Int {
        return EmptyResultViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(inflatedView: View): RecyclerView.ViewHolder {
        return EmptyResultViewHolder(inflatedView, emptyResultListener)
    }

    override fun onBindViewHolder(item: EmptyResultViewModel, viewHolder: BaseViewHolder<EmptyResultViewModel>) {
        viewHolder.setFullSpanStaggeredGrid()

        return super.onBindViewHolder(item, viewHolder)
    }
}