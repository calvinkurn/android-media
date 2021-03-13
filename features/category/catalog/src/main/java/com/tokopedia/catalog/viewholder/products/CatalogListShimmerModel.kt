package com.tokopedia.catalog.viewholder.products

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalog.adapter.factory.CatalogTypeFactory

class CatalogListShimmerModel : Visitable<CatalogTypeFactory> {
    override fun type(typeFactory: CatalogTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}