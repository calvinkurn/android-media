package com.tokopedia.product.manage.feature.quickedit.variant.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory.ProductVariantStockAdapterFactoryImpl
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant

class ProductVariantStockAdapter(
    adapterFactory: ProductVariantStockAdapterFactoryImpl
): BaseListAdapter<ProductVariant, ProductVariantStockAdapterFactoryImpl>(adapterFactory)