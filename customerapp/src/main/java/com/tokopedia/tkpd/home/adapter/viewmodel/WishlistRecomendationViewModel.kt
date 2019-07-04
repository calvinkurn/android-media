package com.tokopedia.tkpd.home.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tkpd.home.adapter.factory.WishlistTypeFactory

/**
 * Author errysuprayogi on 03,July,2019
 */
class WishlistRecomendationViewModel(val recommendationItem: RecommendationItem) : Visitable<WishlistTypeFactory> {

    override fun type(typeFactory: WishlistTypeFactory): Int {
        return typeFactory.type(this)
    }
}
