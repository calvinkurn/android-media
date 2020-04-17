package com.tokopedia.product.manage.feature.quickedit.variant.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory.ProductVariantAdapterFactoryImpl
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant

class ProductVariantAdapter(
    adapterFactory: ProductVariantAdapterFactoryImpl
): BaseListAdapter<ProductVariant, ProductVariantAdapterFactoryImpl>(adapterFactory)