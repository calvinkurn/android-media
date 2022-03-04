package com.tokopedia.catalog.viewholder.products

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalog.adapter.factory.CatalogTypeFactory
import com.tokopedia.catalog.model.raw.CatalogComparisonProductsResponse


class CatalogForYouContainerDataModel() : Visitable<CatalogTypeFactory> {
    override fun type(typeFactory: CatalogTypeFactory): Int {
        return typeFactory.type(this)
    }
}