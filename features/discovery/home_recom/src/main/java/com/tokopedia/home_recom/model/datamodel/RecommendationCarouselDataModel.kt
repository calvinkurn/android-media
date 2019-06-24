package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class RecommendationCarouselDataModel(
        val title: String,
        val products: List<RecommendationItem>,
        val listener: RecommendationCardView.TrackingListener
) : HomeRecommendationDataModel {

    companion object{
        val LAYOUT = R.layout.fragment_recommendation_carousell
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)

}