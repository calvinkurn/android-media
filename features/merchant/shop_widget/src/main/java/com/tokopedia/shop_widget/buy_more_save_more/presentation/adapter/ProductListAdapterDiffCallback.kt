package com.tokopedia.shop_widget.buy_more_save_more.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel.Product

object ProductListAdapterDiffCallback {

    val ProductListDiffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItemProductDisplay: Product, newItemProductDisplay: Product): Boolean {
            return oldItemProductDisplay.productId == newItemProductDisplay.productId
        }

        override fun areContentsTheSame(oldItemProductDisplay: Product, newItemProductDisplay: Product): Boolean {
            return oldItemProductDisplay == newItemProductDisplay
        }
    }
}
