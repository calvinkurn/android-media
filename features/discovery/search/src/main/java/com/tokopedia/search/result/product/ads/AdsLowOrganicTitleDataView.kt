package com.tokopedia.search.result.product.ads

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class AdsLowOrganicTitleDataView(
    val keyword: String,
): Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    companion object {
        fun create(keyword: String) = AdsLowOrganicTitleDataView(keyword)
    }
}
