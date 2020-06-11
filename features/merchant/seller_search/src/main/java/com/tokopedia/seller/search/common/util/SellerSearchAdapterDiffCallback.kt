package com.tokopedia.seller.search.common.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel

object SellerSearchAdapterDiffCallback {

    val ItemSearchDiffCallback = object : DiffUtil.ItemCallback<ItemSellerSearchUiModel>() {
        override fun areItemsTheSame(oldItemSellerSearch: ItemSellerSearchUiModel, newItemSellerSearch: ItemSellerSearchUiModel): Boolean {
            return oldItemSellerSearch == oldItemSellerSearch
        }

        override fun areContentsTheSame(oldItemSellerSearch: ItemSellerSearchUiModel, newItemSellerSearch: ItemSellerSearchUiModel): Boolean {
            return oldItemSellerSearch == oldItemSellerSearch
        }
    }
}