package com.tokopedia.oldcatalog.viewholder.products

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.oldcatalog.adapter.factory.CatalogTypeFactory
import com.tokopedia.oldcatalog.model.raw.CatalogComparisonProductsResponse


class CatalogForYouContainerDataModel() : Visitable<CatalogTypeFactory> {
    override fun type(typeFactory: CatalogTypeFactory): Int {
        return typeFactory.type(this)
    }
}
