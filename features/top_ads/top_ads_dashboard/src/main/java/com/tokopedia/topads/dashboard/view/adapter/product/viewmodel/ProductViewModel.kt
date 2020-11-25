package com.tokopedia.topads.dashboard.view.adapter.product.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.product.ProductAdapterTypeFactory

abstract class ProductViewModel {
    abstract fun type(typesFactory: ProductAdapterTypeFactory): Int
}