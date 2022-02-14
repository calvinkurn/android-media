package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductCardListBinding
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.utils.view.binding.viewBinding

class ListProductItemViewHolder(
    itemView: View,
    productListener: ProductListener
) : ProductItemViewHolder(itemView, productListener) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_card_list
    }
    private var binding: SearchResultProductCardListBinding? by viewBinding()

    override fun bind(productItemData: ProductItemDataView?) {
        val binding = binding ?: return
        if (productItemData == null) return

        with(binding.productCardView) {
            setProductModel(productItemData.toProductCardModel(productItemData.imageUrl, true))

            setThreeDotsOnClickListener {
                productListener.onThreeDotsClick(productItemData, adapterPosition)
            }

            setOnLongClickListener {
                productListener.onThreeDotsClick(productItemData, adapterPosition)
                true
            }

            setOnClickListener {
                productListener.onItemClicked(productItemData, adapterPosition)
            }

            setImageProductViewHintListener(productItemData, createImageProductViewHintListener(productItemData))
        }
    }

    override fun bind(productItemData: ProductItemDataView?, payloads: MutableList<Any>) {
        payloads.getOrNull(0) ?: return

        binding?.productCardView?.setThreeDotsOnClickListener {
            productListener.onThreeDotsClick(productItemData, adapterPosition)
        }
    }
}