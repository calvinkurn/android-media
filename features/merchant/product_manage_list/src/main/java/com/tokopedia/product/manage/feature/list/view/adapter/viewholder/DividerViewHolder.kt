package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.ProductItemDivider

class DividerViewHolder(itemView: View): AbstractViewHolder<ProductItemDivider>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_product_manage_divider
    }

    override fun bind(element: ProductItemDivider?) {}
}