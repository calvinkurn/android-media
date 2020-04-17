package com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder.ProductVariantViewHolder

class ProductVariantAdapterFactoryImpl: BaseAdapterTypeFactory(), ProductVariantAdapterFactory {

    override fun type(viewModel: ProductVariant): Int = ProductVariantViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ProductVariantViewHolder.LAYOUT -> ProductVariantViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }
}