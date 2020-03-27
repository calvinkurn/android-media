package com.tokopedia.product.manage.feature.list.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.EmptyStateViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductViewHolder
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel

class ProductManageAdapterFactoryImpl(
    private val listener: ProductViewHolder.ProductViewHolderView
) : BaseAdapterTypeFactory(), ProductManageAdapterFactory {

    override fun type(viewMode: ProductViewModel): Int = ProductViewHolder.LAYOUT

    override fun type(viewModel: EmptyModel?): Int =  EmptyStateViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProductViewHolder.LAYOUT -> ProductViewHolder(view, listener)
            EmptyStateViewHolder.LAYOUT -> EmptyStateViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}