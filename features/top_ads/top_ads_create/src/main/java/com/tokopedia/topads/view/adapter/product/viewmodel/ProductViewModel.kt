package com.tokopedia.topads.view.adapter.product.viewmodel

import com.tokopedia.topads.view.adapter.product.ProductListAdapterTypeFactory

/**
 * Author errysuprayogi on 12,November,2019
 */
abstract class ProductViewModel {
    abstract fun type(typesFactory: ProductListAdapterTypeFactory): Int
}
