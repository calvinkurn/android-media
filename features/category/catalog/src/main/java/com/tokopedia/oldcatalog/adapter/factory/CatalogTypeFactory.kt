package com.tokopedia.oldcatalog.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.oldcatalog.model.raw.CatalogProductItem
import com.tokopedia.oldcatalog.viewholder.products.CatalogForYouContainerDataModel
import com.tokopedia.oldcatalog.viewholder.products.CatalogListShimmerModel
import com.tokopedia.common_category.factory.BaseProductTypeFactory


interface CatalogTypeFactory : BaseProductTypeFactory, AdapterTypeFactory {

    fun type(listShimmerModel: CatalogListShimmerModel): Int

    fun type(catalogItem: CatalogProductItem): Int

    fun type(model: CatalogForYouContainerDataModel): Int
}
