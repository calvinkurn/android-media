package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class RecommendationItemDataView (
        val recommendationItem: RecommendationItem
): Visitable<ProductListTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }
}