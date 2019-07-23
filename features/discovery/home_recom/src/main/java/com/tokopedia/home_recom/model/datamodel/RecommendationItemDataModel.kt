package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.listener.TrackingListener
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class RecommendationItemDataModel(
        val productItem: RecommendationItem,
        val listener: TrackingListener
) : HomeRecommendationDataModel {

    companion object{
        val LAYOUT = R.layout.fragment_recommendation_item
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)

}