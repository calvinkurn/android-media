package com.tokopedia.product.manage.common.feature.variant.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.common.feature.variant.adapter.factory.ProductVariantAdapterFactory
import com.tokopedia.unifycomponents.ticker.TickerData

data class ProductVariantTicker(
    val tickerList: List<TickerData>
): Visitable<ProductVariantAdapterFactory> {
    override fun type(typeFactory: ProductVariantAdapterFactory): Int {
       return typeFactory.type(this)
    }
}