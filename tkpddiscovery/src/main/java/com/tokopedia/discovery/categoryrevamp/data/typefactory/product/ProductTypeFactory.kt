package com.tokopedia.discovery.categoryrevamp.data.typefactory.product

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.Productshimmer.model.BigListShimmerModel
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.Productshimmer.model.GridListShimmerModel
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.Productshimmer.model.ListShimmerModel
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.data.typefactory.BaseProductTypeFactory


interface ProductTypeFactory : BaseProductTypeFactory, AdapterTypeFactory {

    fun type(productsItem: ProductsItem): Int

    fun type(gridListShimmerModel: GridListShimmerModel): Int

    fun type(listShimmerModel: ListShimmerModel): Int

    fun type(bigListShimmerModel: BigListShimmerModel): Int
}