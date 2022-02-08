package com.tokopedia.shop_widget.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.common.uimodel.ProductCardUiModel

class ProductCardViewHolder(
    itemView: View
): AbstractViewHolder<ProductCardUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_card_flashsale
    }

    override fun bind(element: ProductCardUiModel) {

    }

}