package com.tokopedia.home_recom.model.dataModel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.homerecommendation.HomeRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class RecommendationItemDataModel(
        val productItem: RecommendationItem,
        val listener: RecommendationCardView.TrackingListener
) : BaseHomeRecommendationDataModel {

    companion object{
        val LAYOUT = R.layout.fragment_recommendation_item
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)

}