package com.tokopedia.common_category.viewholders.viewholder.catalogShimmer.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common_category.factory.catalog.CatalogTypeFactory

class GridListCatalogShimmerModel : Visitable<CatalogTypeFactory> {

    override fun type(typeFactory: CatalogTypeFactory): Int {
        return typeFactory.type(this)
    }
}