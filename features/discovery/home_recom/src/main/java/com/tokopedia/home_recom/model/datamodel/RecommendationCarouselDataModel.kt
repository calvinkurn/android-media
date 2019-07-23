package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.listener.TrackingListener
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class RecommendationCarouselDataModel(
        val title: String,
        val products: List<RecommendationCarouselItemDataModel>,
        val listener: TrackingListener
) : HomeRecommendationDataModel {

    companion object{
        val LAYOUT = R.layout.fragment_recommendation_carousell
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)

    fun contains(item: RecommendationItem) = products.asSequence().any { it.productItem.productId == item.productId }

    fun contains(id: Int) = products.asSequence().any { it.productItem.productId == id }
}