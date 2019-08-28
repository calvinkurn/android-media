package com.tokopedia.discovery.categoryrevamp.data.typefactory.product

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.data.typefactory.BaseProductTypeFactory


interface ProductTypeFactory : BaseProductTypeFactory, AdapterTypeFactory {

    fun type(productsItem: ProductsItem): Int
}