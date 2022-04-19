package com.tokopedia.topads.view.adapter.product.viewmodel

import com.tokopedia.topads.view.adapter.product.ProductListAdapterTypeFactory

/**
 * Author errysuprayogi on 12,November,2019
 */
class ProductShimmerViewModel() : ProductViewModel() {

    var isChecked: Boolean = false

    override fun type(typesFactory: ProductListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}