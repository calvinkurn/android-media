package com.tokopedia.tokomart.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView

class ProductItemViewHolder(itemView: View): AbstractViewHolder<ProductItemDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_search_category_product
    }

    private val productCard: ProductCardGridView? =
            itemView.findViewById(R.id.tokomartSearchCategoryProductCard)

    override fun bind(element: ProductItemDataView?) {
        element ?: return

        productCard?.setProductModel(
                ProductCardModel(
                        productImageUrl = element.imageUrl300,
                        productName = element.name,
                        formattedPrice = element.price,
                )
        )
    }
}