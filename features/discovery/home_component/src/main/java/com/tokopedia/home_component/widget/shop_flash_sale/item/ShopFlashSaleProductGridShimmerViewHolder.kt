package com.tokopedia.home_component.widget.shop_flash_sale.item

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R as home_componentR

internal class ShopFlashSaleProductGridShimmerViewHolder(
    itemView: View
): AbstractViewHolder<ShopFlashSaleProductGridShimmerDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.home_component_shop_flash_sale_product_grid_shimmer
    }

    override fun bind(tab: ShopFlashSaleProductGridShimmerDataModel) {

    }
}
