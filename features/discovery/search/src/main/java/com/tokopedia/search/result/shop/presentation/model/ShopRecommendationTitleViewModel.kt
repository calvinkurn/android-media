package com.tokopedia.search.result.shop.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.shop.presentation.typefactory.ShopListTypeFactory

internal class ShopRecommendationTitleViewModel: Visitable<ShopListTypeFactory> {

    override fun type(typeFactory: ShopListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}