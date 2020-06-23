package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.SellerSearchAdapterDiffCallback
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener

class ItemHistorySearchAdapter(private val historySearchListener: HistorySearchListener): ListAdapter<ItemInitialSearchUiModel, ItemHistorySearchViewHolder>(SellerSearchAdapterDiffCallback.ItemInitialSearchDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHistorySearchViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_initial_search_with_history, parent, false)
        return ItemHistorySearchViewHolder(view, historySearchListener)
    }

    override fun onBindViewHolder(holderSearch: ItemHistorySearchViewHolder, position: Int) {
        getItem(position)?.let {
            holderSearch.bind(it)
        }
    }
}