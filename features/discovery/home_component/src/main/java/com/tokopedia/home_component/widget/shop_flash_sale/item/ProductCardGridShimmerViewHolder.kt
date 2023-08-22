package com.tokopedia.home_component.widget.shop_flash_sale.item

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class ProductCardGridShimmerViewHolder(
    itemView: View
): AbstractViewHolder<ProductCardGridShimmerDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.home_component.R.layout.home_component_product_card_grid_shimmer
    }

    override fun bind(tab: ProductCardGridShimmerDataModel) {

    }
}
