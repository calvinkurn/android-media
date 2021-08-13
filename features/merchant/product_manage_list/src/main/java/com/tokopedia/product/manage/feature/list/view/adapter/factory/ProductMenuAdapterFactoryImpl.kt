package com.tokopedia.product.manage.feature.list.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.DividerViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductMenuViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductMenuViewHolder.*
import com.tokopedia.product.manage.feature.list.view.model.ProductItemDivider
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel

class ProductMenuAdapterFactoryImpl(
    private val listener: ProductMenuListener
) : BaseAdapterTypeFactory(), ProductMenuAdapterFactory {

    override fun type(item: ProductMenuUiModel): Int = ProductMenuViewHolder.LAYOUT

    override fun type(item: ProductItemDivider): Int = DividerViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ProductMenuViewHolder.LAYOUT -> ProductMenuViewHolder(parent, listener)
            DividerViewHolder.LAYOUT -> DividerViewHolder(parent)
            else ->  super.createViewHolder(parent, type)
        }
    }
}