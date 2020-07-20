package com.tokopedia.common_category.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.common_category.R
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.interfaces.ProductCardListener
import com.tokopedia.common_category.model.productModel.ProductsItem
import kotlinx.android.synthetic.main.category_product_card_small_grid.view.*

class SmallGridProductCardViewHolder(itemView: View,productCardListener: ProductCardListener) : ProductCardViewHolder(itemView,productCardListener) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.category_product_card_small_grid
    }

    override fun bind(productItem: ProductsItem?) {
        if (productItem == null) return

        itemView.productCardView?.setProductModel(productItem.toProductCardModel(CategoryNavConstants.RecyclerView.GridType.GRID_2))

        itemView.productCardView?.setOnClickListener {
            productListener.onItemClicked(productItem, adapterPosition)
        }

        itemView.productCardView?.setThreeDotsOnClickListener {
            productListener.onThreeDotsClicked(productItem, adapterPosition)
        }
    }
}