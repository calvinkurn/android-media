package com.tokopedia.recommendation_widget_common.widget.comparison

import android.os.Bundle
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationVisitable

/**
 * Created by DevaraFikry on 18/03/21.
 */
data class ComparisonWidgetDataModel(
        val id: String = "",
        val comparisonListModel: ComparisonListModel
) : RecommendationVisitable{
    override fun visitableId(): String? {
        return id
    }

    override fun equalsWith(b: Any?): Boolean {
        return b is ComparisonWidgetDataModel &&
                b.comparisonListModel == comparisonListModel
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    override fun type(typeFactory: RecommendationTypeFactory): Int = typeFactory.type(this)
}