package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class EmptySearchProductDataView(
        var isBannerAdsAllowed: Boolean = true,
        var isFilterActive: Boolean = false,
        var isLocalSearch: Boolean = false,
        var globalSearchApplink: String = "",
        var keyword: String = "",
        var pageTitle: String = "",
) : Visitable<ProductListTypeFactory?> {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}