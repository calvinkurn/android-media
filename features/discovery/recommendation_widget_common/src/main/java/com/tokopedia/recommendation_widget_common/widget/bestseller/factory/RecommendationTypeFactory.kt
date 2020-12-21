package com.tokopedia.recommendation_widget_common.widget.bestseller.factory

import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel

/**
 * Created by Lukas on 05/11/20.
 */
interface RecommendationTypeFactory {
    fun type(bestSellerDataModel: BestSellerDataModel): Int
}