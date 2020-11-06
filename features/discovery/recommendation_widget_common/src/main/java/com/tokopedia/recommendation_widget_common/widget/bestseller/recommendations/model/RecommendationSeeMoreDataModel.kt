package com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.typefactory.RecommendationCarouselTypeFactory

/**
 * Created by Lukas on 05/11/20.
 */
data class RecommendationSeeMoreDataModel (
        val applink: String = "",
        val backgroundImage: String = ""
): Visitable<RecommendationCarouselTypeFactory> {
    override fun type(typeFactory: RecommendationCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }
}