package com.tokopedia.seller.search.common.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ItemSellerSearchUiModel

object AdapterDiffCallback {
    val ItemSearchDiffCallback = object : DiffUtil.ItemCallback<ItemSellerSearchUiModel>() {
        override fun areItemsTheSame(oldItemSearch: ItemSellerSearchUiModel, newItemSearch: ItemSellerSearchUiModel): Boolean {
            return oldItemSearch == newItemSearch
        }

        override fun areContentsTheSame(oldItemSearch: ItemSellerSearchUiModel, newItemSearch: ItemSellerSearchUiModel): Boolean {
            return oldItemSearch == newItemSearch
        }
    }
}