package com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.categoryrevamp.data.typefactory.catalog.CatalogTypeFactory

class BigListCatalogShimmerModel : Visitable<CatalogTypeFactory> {
    override fun type(typeFactory: CatalogTypeFactory): Int {
        return typeFactory.type(this)
    }
}