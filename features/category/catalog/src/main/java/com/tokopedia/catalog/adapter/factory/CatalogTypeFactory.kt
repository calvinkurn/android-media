package com.tokopedia.catalog.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.catalog.model.raw.CatalogProductItem
import com.tokopedia.catalog.viewholder.products.CatalogListShimmerModel
import com.tokopedia.common_category.factory.BaseProductTypeFactory


interface CatalogTypeFactory : BaseProductTypeFactory, AdapterTypeFactory {

    fun type(listShimmerModel: CatalogListShimmerModel): Int

    fun type(catalogItem: CatalogProductItem): Int

}