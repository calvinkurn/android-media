package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductCardBigGridBinding
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.utils.view.binding.viewBinding

class BigGridProductItemViewHolder(
    itemView: View,
    productListener: ProductListener
) : ProductItemViewHolder(itemView, productListener) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_card_big_grid
    }

    private var binding: SearchResultProductCardBigGridBinding? by viewBinding()

    override val productCardView: IProductCardView?
        get() = binding?.productCardView

    override fun bind(productItemData: ProductItemDataView?) {
        if (productItemData == null) return
        val productCardView = binding?.productCardView ?: return

        val productCardModel =
            productItemData.toProductCardModel(productItemData.imageUrl700, true)

        registerLifecycleObserver(productCardModel)

        productCardView.setProductModel(productCardModel)

        productCardView.setThreeDotsOnClickListener {
            productListener.onThreeDotsClick(productItemData, adapterPosition)
        }

        productCardView.setOnLongClickListener {
            productListener.onThreeDotsClick(productItemData, adapterPosition)
            true
        }

        productCardView.setOnClickListener {
            productListener.onItemClicked(productItemData, adapterPosition)
        }

        productCardView.setImageProductViewHintListener(
            productItemData,
            createImageProductViewHintListener(productItemData)
        )
    }
}