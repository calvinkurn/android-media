package com.tokopedia.discovery.categoryrevamp.data.productModel.typefactory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem


interface ProductTypeFactory : BaseProductTypeFactory, AdapterTypeFactory {

    fun type(productsItem: ProductsItem): Int

//    fun createViewHolder(view: View, type: Int): AbstractViewHolder<Visitable<AdapterTypeFactory>>

}