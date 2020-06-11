package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.SellerSearchAdapterDiffCallback
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel

class ItemHistorySearchAdapter: ListAdapter<ItemSellerSearchUiModel, ItemHistorySearchViewHolder>(SellerSearchAdapterDiffCallback.ItemSearchDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHistorySearchViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_initial_search_with_history, parent, false)
        return ItemHistorySearchViewHolder(view)
    }

    override fun onBindViewHolder(holderSearch: ItemHistorySearchViewHolder, position: Int) {
        getItem(position)?.let {
            holderSearch.bind(it)
        }
    }
}