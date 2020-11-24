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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecommendationSeeMoreDataModel) return false

        if (applink != other.applink) return false
        if (backgroundImage != other.backgroundImage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = applink.hashCode()
        result = 31 * result + backgroundImage.hashCode()
        return result
    }

}