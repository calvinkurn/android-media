package com.tokopedia.common_category.data.catalog

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.common_category.factory.BaseProductTypeFactory
import com.tokopedia.common_category.viewholders.CatalogShimmer.model.BigListCatalogShimmerModel
import com.tokopedia.common_category.viewholders.CatalogShimmer.model.GridListCatalogShimmerModel
import com.tokopedia.common_category.viewholders.CatalogShimmer.model.ListCatalogShimmerModel
import com.tokopedia.common_category.data.catalogModel.CatalogItem


interface CatalogTypeFactory : BaseProductTypeFactory, AdapterTypeFactory {

    fun type(catalogItem: CatalogItem): Int

    fun type(gridListShimmerModel: GridListCatalogShimmerModel): Int

    fun type(listShimmerModel: ListCatalogShimmerModel): Int

    fun type(bigListShimmerModel: BigListCatalogShimmerModel): Int

}