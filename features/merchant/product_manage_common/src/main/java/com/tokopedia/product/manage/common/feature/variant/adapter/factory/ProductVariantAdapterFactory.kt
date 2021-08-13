package com.tokopedia.product.manage.common.feature.variant.adapter.factory

import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariantTicker
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant

interface ProductVariantAdapterFactory {

    fun type(viewModel: ProductVariant): Int

    fun type(viewModel: ProductVariantTicker): Int
}