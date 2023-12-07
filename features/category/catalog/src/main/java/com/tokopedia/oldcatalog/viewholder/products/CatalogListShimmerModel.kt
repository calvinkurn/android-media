package com.tokopedia.oldcatalog.viewholder.products

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.oldcatalog.adapter.factory.CatalogTypeFactory

class CatalogListShimmerModel : Visitable<CatalogTypeFactory> {
    override fun type(typeFactory: CatalogTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}
