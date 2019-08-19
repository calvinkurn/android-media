package com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.product

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.discovery.R
import com.tokopedia.productcard.v2.ProductCardView
import kotlinx.android.synthetic.main.product_card_list.view.*

class ListProductCardViewHolder(itemView: View) : ProductCardViewHolder(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.product_card_list
    }


    override fun getProductCardView(): ProductCardView? {
        return itemView.productCardView ?: null
    }

    override fun isUsingBigImageUrl(): Boolean {
        return false
    }
}