package com.tokopedia.discovery.categoryrevamp.data.typefactory.catalog

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.model.BigListCatalogShimmerModel
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.model.GridListCatalogShimmerModel
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.model.ListCatalogShimmerModel
import com.tokopedia.discovery.categoryrevamp.data.catalogModel.CatalogItem
import com.tokopedia.discovery.categoryrevamp.data.typefactory.BaseProductTypeFactory


interface CatalogTypeFactory : BaseProductTypeFactory, AdapterTypeFactory {

    fun type(catalogItem: CatalogItem): Int

    fun type(gridListShimmerModel: GridListCatalogShimmerModel): Int

    fun type(listShimmerModel: ListCatalogShimmerModel): Int

    fun type(bigListShimmerModel: BigListCatalogShimmerModel): Int

}