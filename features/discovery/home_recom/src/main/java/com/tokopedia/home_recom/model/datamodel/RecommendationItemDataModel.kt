package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.home_recom.view.viewholder.RecommendationItemViewHolder
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * A Class of DataModel.
 *
 * This class for holding data for type factory pattern [RecommendationItemViewHolder]
 * @param productItem the pojo of product recommendation from the network
 * @param listener the default listener for recommendation widget, it will handling on impression, click, wishlist tracker
 */
data class RecommendationItemDataModel(
        val productItem: RecommendationItem,
        val listener: RecommendationListener
) : HomeRecommendationDataModel {

    companion object{
        val LAYOUT = R.layout.fragment_recommendation_item
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)

}