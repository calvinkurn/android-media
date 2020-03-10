package com.tokopedia.similarsearch.divider

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.similarsearch.abstraction.BaseAdapterDelegate
import com.tokopedia.similarsearch.abstraction.BaseViewHolder
import com.tokopedia.similarsearch.utils.setFullSpanStaggeredGrid

internal class DividerAdapterDelegate: BaseAdapterDelegate<DividerViewModel, DividerViewHolder>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is DividerViewModel
    }

    override fun getViewHolderLayout(): Int {
        return DividerViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(inflatedView: View): RecyclerView.ViewHolder {
        return DividerViewHolder(inflatedView)
    }

    override fun onBindViewHolder(item: DividerViewModel, viewHolder: BaseViewHolder<DividerViewModel>) {
        viewHolder.setFullSpanStaggeredGrid()

        super.onBindViewHolder(item, viewHolder)
    }
}