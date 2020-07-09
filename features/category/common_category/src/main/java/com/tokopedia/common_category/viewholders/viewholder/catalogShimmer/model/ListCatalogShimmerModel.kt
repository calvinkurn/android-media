package com.tokopedia.common_category.viewholders.viewholder.catalogShimmer.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common_category.factory.catalog.CatalogTypeFactory

class ListCatalogShimmerModel : Visitable<CatalogTypeFactory> {
    override fun type(typeFactory: CatalogTypeFactory): Int {
        return typeFactory.type(this)
    }

}