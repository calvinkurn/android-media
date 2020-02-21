package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlinx.android.synthetic.main.search_result_product_card_list.view.*

class ListProductItemViewHolder(
    itemView: View,
    productListener: ProductListener
) : ProductItemViewHolder(itemView, productListener) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_card_list
    }

    override fun bind(productItem: ProductItemViewModel?) {
        if (productItem == null) return

        itemView.productCardView?.setProductModel(productItem.toProductCardModel(false))

        itemView.productCardView?.setThreeDotsOnClickListener {
            productListener.onLongClick(productItem, adapterPosition)
        }

        itemView.productCardView?.setOnClickListener {
            productListener.onItemClicked(productItem, adapterPosition)
        }

        itemView.productCardView?.setImageProductViewHintListener(productItem, createImageProductViewHintListener(productItem))
    }
}