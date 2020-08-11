package com.tokopedia.seller.search.feature.suggestion.view.viewholder.faq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.AdapterDiffCallback
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.FaqSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ItemSellerSearchUiModel

class ItemFaqSearchAdapter(private val faqSearchListener: FaqSearchListener):
        ListAdapter<ItemSellerSearchUiModel, ItemFaqSearchViewHolder>(AdapterDiffCallback.ItemSearchDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFaqSearchViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_result_faq, parent, false)
        return ItemFaqSearchViewHolder(view, faqSearchListener)
    }

    override fun onBindViewHolder(holderSearch: ItemFaqSearchViewHolder, position: Int) {
        getItem(position)?.let { holderSearch.bind(it) }
    }
}