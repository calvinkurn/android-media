package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.history

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.databinding.ItemInitialSearchWithHistoryBinding
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener
import com.tokopedia.utils.view.binding.viewBinding

class HistorySearchViewHolder(
    view: View,
    private val historySearchListener: HistorySearchListener
) :
    AbstractViewHolder<ItemInitialSearchUiModel>(view) {

    companion object {
        val LAYOUT_RES = R.layout.item_initial_search_with_history
    }

    private val binding: ItemInitialSearchWithHistoryBinding? by viewBinding()

    override fun bind(element: ItemInitialSearchUiModel) {
        binding?.run {
            tvTitleHistory.text = element.title

            ivCloseHistory.setOnClickListener {
                historySearchListener.onClearSearchItem(element.title.orEmpty(), adapterPosition)
            }
            root.setOnClickListener {
                historySearchListener.onHistoryItemClicked(element.title.orEmpty())
            }
        }
    }
}