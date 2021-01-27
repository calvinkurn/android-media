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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecommendationCarouselItemDataModel) return false

        if (recommendationItem != other.recommendationItem) return false
        if (productCardModel != other.productCardModel) return false

        return true
    }

    override fun hashCode(): Int {
        var result = recommendationItem.hashCode()
        result = 31 * result + productCardModel.hashCode()
        return result
    }

}