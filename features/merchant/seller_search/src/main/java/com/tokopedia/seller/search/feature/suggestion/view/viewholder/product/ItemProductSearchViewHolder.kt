package com.tokopedia.seller.search.feature.suggestion.view.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.bindTitleText
import com.tokopedia.seller.search.databinding.ItemSearchResultProductBinding
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.ProductSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ProductSellerSearchUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemProductSearchViewHolder(
    itemViewProduct: View,
    private val productSearchListener: ProductSearchListener
) : AbstractViewHolder<ProductSellerSearchUiModel>(itemViewProduct) {

    companion object {
        val LAYOUT = R.layout.item_search_result_product
    }

    private val binding: ItemSearchResultProductBinding? by viewBinding()

    override fun bind(element: ProductSellerSearchUiModel) {
        binding?.run {
            ivSearchResultProduct.setImageUrl(element.imageUrl.orEmpty())
            tvSearchResultProductTitle.bindTitleText(
                element.title.orEmpty(),
                element.keyword.orEmpty()
            )
            tvSearchResultProductDesc.text = element.desc

            root.setOnClickListener {
                productSearchListener.onProductItemClicked(element, adapterPosition)
            }
        }
    }
}