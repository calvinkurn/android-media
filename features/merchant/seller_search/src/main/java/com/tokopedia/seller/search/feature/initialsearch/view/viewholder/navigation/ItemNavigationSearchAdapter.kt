package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.SellerSearchAdapterDiffCallback
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel

class ItemNavigationSearchAdapter: ListAdapter<ItemSellerSearchUiModel, ItemNavigationSearchViewHolder>(SellerSearchAdapterDiffCallback.ItemSearchDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemNavigationSearchViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_result_navigation, parent, false)
        return ItemNavigationSearchViewHolder(view)
    }

    override fun onBindViewHolder(holderSearch: ItemNavigationSearchViewHolder, position: Int) {
        getItem(position)?.let {
            holderSearch.bind(it)
        }
    }
}