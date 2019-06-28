package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.view.listener.ProductListener

class ListProductItemViewHolder(
    itemView: View,
    productListener: ProductListener
) : ProductItemViewHolder(itemView, productListener) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_big_grid_product_card
    }

    override fun getProductCardView(): ProductCardView? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isUsingBigImageUrl(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}