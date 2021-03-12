package com.tokopedia.topads.dashboard.view.adapter.product.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.product.ProductAdapterTypeFactory

/**
 * Created by Pika on 7/6/20.
 */
class ProductEmptyModel : ProductModel() {

    override fun type(typesFactory: ProductAdapterTypeFactory): Int {
        return typesFactory.type(this)

    }
}