package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ProductGridListPlaceholderUiModel

class ProductGridListPlaceholderViewHolder(
    itemView: View
) : AbstractViewHolder<ProductGridListPlaceholderUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_grid_list_shimmering
    }

    override fun bind(model: ProductGridListPlaceholderUiModel) {}
}
