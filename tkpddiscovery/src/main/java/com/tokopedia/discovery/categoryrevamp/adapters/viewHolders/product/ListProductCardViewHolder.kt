package com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.view.interfaces.ProductCardListener
import kotlinx.android.synthetic.main.category_product_card_list.view.*

class ListProductCardViewHolder(itemView: View,productCardListener: ProductCardListener) : ProductCardViewHolder(itemView,productCardListener) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.category_product_card_list
    }

    override fun bind(productItem: ProductsItem?) {
        if (productItem == null) return

        itemView.productCardView?.setProductModel(productItem.toProductCardModel(false))

        itemView.productCardView?.setOnClickListener {
            productListener.onItemClicked(productItem, adapterPosition)
        }

        itemView.productCardView?.setThreeDotsOnClickListener {
            productListener.onThreeDotsClicked(productItem, adapterPosition)
        }
    }
}