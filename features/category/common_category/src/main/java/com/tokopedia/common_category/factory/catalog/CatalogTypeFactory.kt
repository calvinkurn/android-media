package com.tokopedia.common_category.factory.catalog

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.common_category.factory.BaseProductTypeFactory
import com.tokopedia.common_category.data.catalogModel.CatalogItem


interface CatalogTypeFactory : BaseProductTypeFactory, AdapterTypeFactory {

    fun type(catalogItem: CatalogItem): Int
}