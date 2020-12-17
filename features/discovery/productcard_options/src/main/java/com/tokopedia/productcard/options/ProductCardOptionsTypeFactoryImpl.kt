package com.tokopedia.productcard.options

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.options.divider.ProductCardOptionsDividerViewHolder
import com.tokopedia.productcard.options.divider.ProductCardOptionsItemDivider
import com.tokopedia.productcard.options.item.ProductCardOptionsItemModel
import com.tokopedia.productcard.options.item.ProductCardOptionsItemViewHolder

internal class ProductCardOptionsTypeFactoryImpl: ProductCardOptionsTypeFactory {

    override fun type(viewModel: ProductCardOptionsItemModel): Int {
        return ProductCardOptionsItemViewHolder.LAYOUT
    }

    override fun type(viewModel: ProductCardOptionsItemDivider): Int {
        return ProductCardOptionsDividerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            ProductCardOptionsItemViewHolder.LAYOUT -> ProductCardOptionsItemViewHolder(parent)
            ProductCardOptionsDividerViewHolder.LAYOUT -> ProductCardOptionsDividerViewHolder(parent)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        }
    }
}