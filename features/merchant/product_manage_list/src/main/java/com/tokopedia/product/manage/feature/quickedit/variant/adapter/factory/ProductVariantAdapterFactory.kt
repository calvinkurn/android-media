package com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory

import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductTicker
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant

interface ProductVariantAdapterFactory {

    fun type(viewModel: ProductVariant): Int

    fun type(viewModel: ProductTicker): Int
}