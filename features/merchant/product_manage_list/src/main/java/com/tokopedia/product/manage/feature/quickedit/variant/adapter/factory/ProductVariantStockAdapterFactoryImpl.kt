package com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductTicker
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder.ProductVariantStockTickerViewHolder
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder.ProductVariantStockViewHolder
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder.ProductVariantStockViewHolder.*

class ProductVariantStockAdapterFactoryImpl(
    private val listener: ProductVariantStockListener
): BaseAdapterTypeFactory(), ProductVariantAdapterFactory {

    override fun type(viewModel: ProductVariant): Int = ProductVariantStockViewHolder.LAYOUT

    override fun type(viewModel: ProductTicker): Int = ProductVariantStockTickerViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ProductVariantStockViewHolder.LAYOUT -> ProductVariantStockViewHolder(parent, listener)
            ProductVariantStockTickerViewHolder.LAYOUT -> ProductVariantStockTickerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}