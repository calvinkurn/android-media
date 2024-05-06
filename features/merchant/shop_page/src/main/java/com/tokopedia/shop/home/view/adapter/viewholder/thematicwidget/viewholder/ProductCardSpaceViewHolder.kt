package com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.thematicwidget.ProductCardSpaceUiModel

class ProductCardSpaceViewHolder(
    itemView: View
): AbstractViewHolder<ProductCardSpaceUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_card_space
    }

    override fun bind(element: ProductCardSpaceUiModel) {
        /* nothing to do */
    }
}
