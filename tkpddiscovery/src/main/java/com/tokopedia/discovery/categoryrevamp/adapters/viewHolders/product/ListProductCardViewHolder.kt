package com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.product

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.view.interfaces.ProductCardListener
import com.tokopedia.productcard.v2.ProductCardView
import kotlinx.android.synthetic.main.category_product_card_list.view.*

class ListProductCardViewHolder(itemView: View,productCardListener: ProductCardListener) : ProductCardViewHolder(itemView,productCardListener) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.category_product_card_list
    }


    override fun getProductCardView(): ProductCardView? {
        return itemView.productCardView ?: null
    }

    override fun isUsingBigImageUrl(): Boolean {
        return false
    }
}