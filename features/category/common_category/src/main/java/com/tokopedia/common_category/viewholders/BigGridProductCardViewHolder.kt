package com.tokopedia.common_category.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.common_category.R
import com.tokopedia.common_category.model.productModel.ProductsItem
import kotlinx.android.synthetic.main.category_product_card_big_grid.view.*

class BigGridProductCardViewHolder(itemView: View,productCardListener: com.tokopedia.common_category.interfaces.ProductCardListener) : ProductCardViewHolder(itemView,productCardListener) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.category_product_card_big_grid
    }

    override fun bind(productItem: ProductsItem?) {
        if (productItem == null) return

        itemView.productCardView?.setProductModel(productItem.toProductCardModel(true))

        itemView.productCardView?.setOnClickListener {
            productListener.onItemClicked(productItem, adapterPosition)
        }

        itemView.productCardView?.setThreeDotsOnClickListener {
            productListener.onThreeDotsClicked(productItem, adapterPosition)
        }
    }
}