package com.tokopedia.seller.search.feature.suggestion.view.viewholder.product

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.indexOfSearchQuery
import com.tokopedia.seller.search.common.util.safeSetSpan
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.ProductSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ItemSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ProductSellerSearchUiModel
import kotlinx.android.synthetic.main.item_search_result_product.view.*

class ItemProductSearchViewHolder(
        itemViewProduct: View,
        private val productSearchListener: ProductSearchListener
): AbstractViewHolder<ProductSellerSearchUiModel>(itemViewProduct) {

    companion object {
        val LAYOUT = R.layout.item_search_result_product
    }

    override fun bind(element: ProductSellerSearchUiModel) {
        with(itemView) {
            ivSearchResultProduct?.setImageUrl(element.imageUrl.orEmpty())
            bindTitleText(element)
            tvSearchResultProductDesc?.text = element.desc

            setOnClickListener {
                productSearchListener.onProductItemClicked(element, adapterPosition)
            }
        }
    }

    private fun bindTitleText(item: ItemSellerSearchUiModel) {
        val startIndex = indexOfSearchQuery(item.title.orEmpty(), item.keyword.orEmpty())
        with(itemView) {
            if (startIndex == -1) {
                tvSearchResultProductTitle?.text = item.title
            } else {
                val highlightedTitle = SpannableString(item.title)
                highlightedTitle.safeSetSpan(TextAppearanceSpan(context, R.style.searchTextHiglight),
                        0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                highlightedTitle.safeSetSpan(TextAppearanceSpan(context, R.style.searchTextHiglight),
                        startIndex + item.keyword?.length.orZero(),
                        item.title?.length.orZero(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tvSearchResultProductTitle?.text = highlightedTitle
            }
        }
    }

}