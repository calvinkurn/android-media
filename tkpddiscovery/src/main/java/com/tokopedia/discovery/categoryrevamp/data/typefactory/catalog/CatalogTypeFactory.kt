package com.tokopedia.discovery.categoryrevamp.data.typefactory.catalog

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.catalogModel.CatalogItem
import com.tokopedia.discovery.categoryrevamp.data.typefactory.BaseProductTypeFactory


interface CatalogTypeFactory : BaseProductTypeFactory, AdapterTypeFactory {

    fun type(catalogItem: CatalogItem): Int

}