package com.tokopedia.search.result.presentation.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.result.presentation.model.InspirationCardOptionDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCardOptionChipViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCardOptionRelatedViewHolder
import com.tokopedia.search.result.presentation.view.listener.InspirationCardListener

class InspirationCardOptionAdapter(
        private val inspirationCardListener: InspirationCardListener,
        private val spanCount: Int = 1,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemList = mutableListOf<InspirationCardOptionDataView>()

    fun setItemList(itemList: List<InspirationCardOptionDataView>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)

        notifyItemRangeInserted(0, itemList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return viewType.doWhenViewType({
            InspirationCardOptionChipViewHolder(view, inspirationCardListener)
        }, {
            InspirationCardOptionRelatedViewHolder(view, inspirationCardListener)
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val inspirationCarouselOptionViewModel = itemList[position]

        holder.itemViewType.doWhenViewType({
            if (holder is InspirationCardOptionChipViewHolder)
                holder.bind(inspirationCarouselOptionViewModel)
        }, {
            if (holder is InspirationCardOptionRelatedViewHolder)
                holder.bind(inspirationCarouselOptionViewModel, spanCount)
        })
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList.getOrNull(position)?.isRelated() == true)
            InspirationCardOptionRelatedViewHolder.LAYOUT
        else
            InspirationCardOptionChipViewHolder.LAYOUT
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun <T> Int.doWhenViewType(actionChip: () -> T, actionRelated: () -> T): T {
        return when (this) {
            InspirationCardOptionRelatedViewHolder.LAYOUT -> actionRelated()
            else -> actionChip()
        }
    }
}