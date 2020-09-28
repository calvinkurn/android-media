package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.home_recom.view.adapter.SimilarProductRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by Lukas on 30/08/19
 */
data class SimilarProductRecommendationItemDataModel(
        val productItem: RecommendationItem,
        val listener: RecommendationListener
) : SimilarProductRecommendationDataModel {

    override fun type(typeFactory: SimilarProductRecommendationTypeFactory): Int = typeFactory.type(this)

    companion object{
        val LAYOUT = R.layout.fragment_recommendation_item
    }

}