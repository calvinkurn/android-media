package com.tokopedia.recommendation_widget_common.widget.bestseller.factory

import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetDataModel

/**
 * Created by Lukas on 05/11/20.
 */
interface RecommendationTypeFactory {
    fun type(bestSellerDataModel: BestSellerDataModel): Int
}