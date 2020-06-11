package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.SellerSearchAdapterDiffCallback
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.OrderSearchListener

class ItemOrderSearchAdapter(private val orderSearchListener: OrderSearchListener): ListAdapter<ItemSellerSearchUiModel, ItemOrderSearchViewHolder>(SellerSearchAdapterDiffCallback.ItemSearchDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemOrderSearchViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_result_order, parent, false)
        return ItemOrderSearchViewHolder(view, orderSearchListener)
    }

    override fun onBindViewHolder(holderSearch: ItemOrderSearchViewHolder, position: Int) {
        getItem(position)?.let {
            holderSearch.bind(it)
        }
    }
}