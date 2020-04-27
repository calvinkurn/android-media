package com.tokopedia.similarsearch.title

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.similarsearch.abstraction.BaseAdapterDelegate
import com.tokopedia.similarsearch.abstraction.BaseViewHolder
import com.tokopedia.similarsearch.utils.setFullSpanStaggeredGrid

internal class TitleAdapterDelegate: BaseAdapterDelegate<TitleViewModel, TitleViewHolder>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is TitleViewModel
    }

    override fun getViewHolderLayout(): Int {
        return TitleViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(inflatedView: View): RecyclerView.ViewHolder {
        return TitleViewHolder(inflatedView)
    }

    override fun onBindViewHolder(item: TitleViewModel, viewHolder: BaseViewHolder<TitleViewModel>) {
        viewHolder.setFullSpanStaggeredGrid()

        super.onBindViewHolder(item, viewHolder)
    }
}