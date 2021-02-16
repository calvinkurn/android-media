package com.tokopedia.product.manage.common.feature.variant.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariantTicker
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.adapter.viewholder.ProductVariantTickerViewHolder
import com.tokopedia.product.manage.common.feature.variant.adapter.viewholder.ProductVariantStockViewHolder

class ProductVariantStockAdapterFactoryImpl(
    private val listener: ProductVariantStockViewHolder.ProductVariantStockListener
): BaseAdapterTypeFactory(), ProductVariantAdapterFactory {

    override fun type(viewModel: ProductVariant): Int = ProductVariantStockViewHolder.LAYOUT

    override fun type(viewModel: ProductVariantTicker): Int = ProductVariantTickerViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ProductVariantStockViewHolder.LAYOUT -> ProductVariantStockViewHolder(parent, listener)
            ProductVariantTickerViewHolder.LAYOUT -> ProductVariantTickerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}