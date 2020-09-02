package com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductTicker
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder.ProductVariantPriceViewHolder
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder.ProductVariantPriceViewHolder.*
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder.ProductVariantStockTickerViewHolder

class ProductVariantPriceAdapterFactoryImpl(
    private val listener: ProductVariantListener
): BaseAdapterTypeFactory(), ProductVariantAdapterFactory {

    override fun type(viewModel: ProductVariant): Int = ProductVariantPriceViewHolder.LAYOUT

    override fun type(viewModel: ProductTicker): Int = ProductVariantStockTickerViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ProductVariantPriceViewHolder.LAYOUT -> ProductVariantPriceViewHolder(parent, listener)
            else -> return super.createViewHolder(parent, type)
        }
    }
}