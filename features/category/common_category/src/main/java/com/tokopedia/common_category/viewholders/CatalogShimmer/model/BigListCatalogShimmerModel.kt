package com.tokopedia.common_category.viewholders.CatalogShimmer.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common_category.data.catalog.CatalogTypeFactory

class BigListCatalogShimmerModel : Visitable<CatalogTypeFactory> {
    override fun type(typeFactory: CatalogTypeFactory): Int {
        return typeFactory.type(this)
    }
}