package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlinx.android.synthetic.main.search_result_product_card_small_grid.view.*

class SmallGridProductItemViewHolder(
    itemView: View,
    productListener: ProductListener
) : ProductItemViewHolder(itemView, productListener) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_card_small_grid
    }

    override fun getProductCardView(): ProductCardView? {
        return itemView.productCardView ?: null
    }

    override fun isUsingBigImageUrl(): Boolean {
        return false
    }
}