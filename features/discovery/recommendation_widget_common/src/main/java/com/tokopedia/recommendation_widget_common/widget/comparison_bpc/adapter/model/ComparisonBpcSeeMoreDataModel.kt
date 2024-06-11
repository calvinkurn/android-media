package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.typefactory.ComparisonBpcTypeFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel

/**
 * Created by Frenzel
 */
data class ComparisonBpcSeeMoreDataModel(
    val recomPageName: String,
    val trackingModel: RecommendationWidgetTrackingModel,
    val productAnchor: ComparisonBpcItemModel?
) : Visitable<ComparisonBpcTypeFactory> {
    override fun type(typeFactory: ComparisonBpcTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ComparisonBpcSeeMoreDataModel) return false

        return true
    }

    override fun hashCode(): Int {
        return this.hashCode()
    }
}
