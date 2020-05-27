package com.tokopedia.topads.edit.view.adapter.product.viewmodel

import com.tokopedia.topads.edit.view.adapter.product.ProductListAdapterTypeFactory


abstract class ProductViewModel {
    abstract fun type(typesFactory: ProductListAdapterTypeFactory): Int
}
