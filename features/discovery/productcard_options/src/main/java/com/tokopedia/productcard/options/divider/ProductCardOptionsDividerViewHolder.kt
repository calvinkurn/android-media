package com.tokopedia.productcard.options.divider

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.options.R

internal class ProductCardOptionsDividerViewHolder(
        view: View
): AbstractViewHolder<ProductCardOptionsItemDivider>(view) {

    companion object {
        val LAYOUT = R.layout.product_card_options_item_divider
    }

    override fun bind(element: ProductCardOptionsItemDivider?) {

    }
}