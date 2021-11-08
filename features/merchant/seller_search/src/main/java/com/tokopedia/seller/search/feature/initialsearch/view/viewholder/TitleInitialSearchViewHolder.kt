package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.databinding.InitialSearchWithHistorySectionBinding
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleInitialSearchUiModel
import com.tokopedia.utils.view.binding.viewBinding

class TitleInitialSearchViewHolder(
    view: View,
    private val sellerSearchListener: HistorySearchListener
) : AbstractViewHolder<ItemTitleInitialSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.initial_search_with_history_section
    }

    private val binding: InitialSearchWithHistorySectionBinding? by viewBinding()

    override fun bind(element: ItemTitleInitialSearchUiModel?) {
        binding?.run {
            tvClearAll.setOnClickListener {
                sellerSearchListener.onClearAllSearch()
            }
        }
    }
}