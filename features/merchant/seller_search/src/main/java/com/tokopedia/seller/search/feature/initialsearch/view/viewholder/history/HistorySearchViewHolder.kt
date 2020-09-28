package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.history

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener
import kotlinx.android.synthetic.main.item_initial_search_with_history.view.*

class HistorySearchViewHolder(view: View,
                              private val historySearchListener: HistorySearchListener) :
        AbstractViewHolder<ItemInitialSearchUiModel>(view) {

    companion object {
        val LAYOUT_RES = R.layout.item_initial_search_with_history
    }

    override fun bind(element: ItemInitialSearchUiModel) {
        with(itemView) {
            tvTitleHistory?.text = element.title

            ivCloseHistory?.setOnClickListener {
                historySearchListener.onClearSearchItem(element.title.orEmpty(), adapterPosition)
            }
            setOnClickListener {
                historySearchListener.onHistoryItemClicked(element.title.orEmpty())
            }
        }
    }
}