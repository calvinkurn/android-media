package com.tokopedia.productcard.options

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.options.divider.ProductCardOptionsItemDivider
import com.tokopedia.productcard.options.item.ProductCardOptionsItemModel

internal interface ProductCardOptionsTypeFactory {
    fun type(viewModel: ProductCardOptionsItemModel): Int

    fun type(viewModel: ProductCardOptionsItemDivider): Int

    fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*>
}