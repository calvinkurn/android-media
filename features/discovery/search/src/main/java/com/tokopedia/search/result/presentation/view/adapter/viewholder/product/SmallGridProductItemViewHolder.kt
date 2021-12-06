package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductCardSmallGridBinding
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.utils.view.binding.viewBinding

class SmallGridProductItemViewHolder(
    itemView: View,
    productListener: ProductListener
) : ProductItemViewHolder(itemView, productListener) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_card_small_grid
    }

    private var binding: SearchResultProductCardSmallGridBinding? by viewBinding()

    override fun bind(productItemData: ProductItemDataView?) {
        if (productItemData == null) return

        binding?.productCardView?.let {
            it.setProductModel(productItemData.toProductCardModel(productItemData.imageUrl300, false))

            it.setThreeDotsOnClickListener {
                productListener.onThreeDotsClick(productItemData, adapterPosition)
            }

            it.setOnLongClickListener {
                productListener.onThreeDotsClick(productItemData, adapterPosition)
                true
            }

            it.setOnClickListener {
                productListener.onItemClicked(productItemData, adapterPosition)
            }

            it.setImageProductViewHintListener(productItemData, createImageProductViewHintListener(productItemData))
        }
    }

    override fun bind(productItemData: ProductItemDataView?, payloads: MutableList<Any>) {
        payloads.getOrNull(0) ?: return

        binding?.productCardView?.setThreeDotsOnClickListener {
            productListener.onThreeDotsClick(productItemData, adapterPosition)
        }
    }

    override fun onViewRecycled() {
        binding?.productCardView?.recycle()
    }
}