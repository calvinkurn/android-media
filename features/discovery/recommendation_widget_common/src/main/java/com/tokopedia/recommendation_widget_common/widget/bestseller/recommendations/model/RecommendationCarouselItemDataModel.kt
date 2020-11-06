package com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.typefactory.RecommendationCarouselTypeFactory

/**
 * Created by Lukas on 05/11/20.
 */
data class RecommendationCarouselItemDataModel(
    val recommendationItem: RecommendationItem,
    val productCardModel: ProductCardModel
): Visitable<RecommendationCarouselTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: RecommendationCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }
}