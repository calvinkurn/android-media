package com.tokopedia.common_category.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.common_category.model.productModel.ProductsItem
import com.tokopedia.common_category.model.shimmer.BigListShimmerModel
import com.tokopedia.common_category.model.shimmer.GridListShimmerModel
import com.tokopedia.common_category.model.shimmer.ListShimmerModel


interface ProductTypeFactory : BaseProductTypeFactory, AdapterTypeFactory {

    fun type(productsItem: ProductsItem): Int

    fun type(gridListShimmerModel: GridListShimmerModel): Int

    fun type(listShimmerModel: ListShimmerModel): Int

    fun type(bigListShimmerModel: BigListShimmerModel): Int
}