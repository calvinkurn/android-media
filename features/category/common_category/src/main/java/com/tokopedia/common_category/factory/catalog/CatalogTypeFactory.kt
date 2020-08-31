package com.tokopedia.common_category.factory.catalog

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.common_category.factory.BaseProductTypeFactory
import com.tokopedia.common_category.data.catalogModel.CatalogItem
import com.tokopedia.common_category.viewholders.viewholder.catalogShimmer.model.BigListCatalogShimmerModel
import com.tokopedia.common_category.viewholders.viewholder.catalogShimmer.model.GridListCatalogShimmerModel
import com.tokopedia.common_category.viewholders.viewholder.catalogShimmer.model.ListCatalogShimmerModel


interface CatalogTypeFactory : BaseProductTypeFactory, AdapterTypeFactory {

    fun type(catalogItem: CatalogItem): Int

    fun type(gridListShimmerModel: GridListCatalogShimmerModel): Int

    fun type(listShimmerModel: ListCatalogShimmerModel): Int

    fun type(bigListShimmerModel: BigListCatalogShimmerModel): Int

}