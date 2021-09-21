package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class SearchProductTitleDataView(
        val title: String,
        val isRecommendationTitle: Boolean,
): Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
