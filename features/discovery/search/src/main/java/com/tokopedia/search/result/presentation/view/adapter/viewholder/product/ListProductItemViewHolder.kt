package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlinx.android.synthetic.main.search_result_product_card_list.view.productCardView

class ListProductItemViewHolder(
    itemView: View,
    productListener: ProductListener
) : ProductItemViewHolder(itemView, productListener) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_card_list
    }

    override fun bind(productItemData: ProductItemDataView?) {
        if (productItemData == null) return

        itemView.productCardView?.setProductModel(productItemData.toProductCardModel(productItemData.imageUrl))

        itemView.productCardView?.setThreeDotsOnClickListener {
            productListener.onThreeDotsClick(productItemData, adapterPosition)
        }

        itemView.productCardView?.setOnLongClickListener {
            productListener.onThreeDotsClick(productItemData, adapterPosition)
            true
        }

        itemView.productCardView?.setOnClickListener {
            productListener.onItemClicked(productItemData, adapterPosition)
        }

        itemView.productCardView?.setImageProductViewHintListener(productItemData, createImageProductViewHintListener(productItemData))
    }

    override fun bind(productItemData: ProductItemDataView?, payloads: MutableList<Any>) {
        payloads.getOrNull(0) ?: return

        itemView.productCardView?.setThreeDotsOnClickListener {
            productListener.onThreeDotsClick(productItemData, adapterPosition)
        }
    }
}