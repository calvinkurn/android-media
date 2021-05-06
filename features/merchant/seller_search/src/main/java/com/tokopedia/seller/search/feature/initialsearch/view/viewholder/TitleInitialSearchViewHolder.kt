package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleInitialSearchUiModel
import kotlinx.android.synthetic.main.initial_search_with_history_section.view.*

class TitleInitialSearchViewHolder(view: View,
                                   private val sellerSearchListener: HistorySearchListener):
        AbstractViewHolder<ItemTitleInitialSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.initial_search_with_history_section
    }

    override fun bind(element: ItemTitleInitialSearchUiModel?) {
        with(itemView) {
            tvClearAll?.setOnClickListener {
                sellerSearchListener.onClearAllSearch()
            }
        }
    }
}