package com.tokopedia.shop_widget.thematicwidget.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardSpaceUiModel

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