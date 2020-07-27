package com.tokopedia.topads.edit.view.adapter.product.viewmodel

import com.tokopedia.topads.edit.view.adapter.product.ProductListAdapterTypeFactory


class ProductShimmerViewModel() : ProductViewModel() {

    var isChecked: Boolean = false

    override fun type(typesFactory: ProductListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}