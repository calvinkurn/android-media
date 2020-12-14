package com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.common.feature.variant.adapter.factory.ProductVariantAdapterFactory
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariantTicker
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.adapter.viewholder.ProductVariantTickerViewHolder
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder.ProductVariantPriceViewHolder
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder.ProductVariantPriceViewHolder.*

class ProductVariantPriceAdapterFactoryImpl(
    private val listener: ProductVariantListener
): BaseAdapterTypeFactory(), ProductVariantAdapterFactory {

    private val variantPriceMap: MutableMap<String, Int> = mutableMapOf()

    override fun type(viewModel: ProductVariant): Int = ProductVariantPriceViewHolder.LAYOUT

    override fun type(viewModel: ProductVariantTicker): Int = ProductVariantTickerViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ProductVariantPriceViewHolder.LAYOUT -> ProductVariantPriceViewHolder(parent, variantPriceMap, listener)
            else -> return super.createViewHolder(parent, type)
        }
    }
}