package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.SellerSearchAdapterDiffCallback
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.ProductSearchListener

class ItemProductSearchAdapter(private val productSearchListener: ProductSearchListener?): ListAdapter<ItemSellerSearchUiModel, ItemProductSearchViewHolder>(SellerSearchAdapterDiffCallback.ItemSearchDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemProductSearchViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_result_product, parent, false)
        return ItemProductSearchViewHolder(view, productSearchListener)
    }

    override fun onBindViewHolder(holderSearch: ItemProductSearchViewHolder, position: Int) {
        getItem(position)?.let {
            holderSearch.bind(it)
        }
    }
}