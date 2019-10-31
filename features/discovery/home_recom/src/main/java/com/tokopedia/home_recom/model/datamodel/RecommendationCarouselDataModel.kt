package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.home_recom.view.viewholder.RecommendationCarouselViewHolder
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * A Class of DataModel.
 *
 * This class for holding data for type factory pattern [RecommendationCarouselViewHolder]
 * @param title the title of widget recommendation carousel
 * @param products the list of recommendation item, it hold data for carousel
 * @param listener the default listener for recommendation widget, it will handling on impression, click, wishlist tracker
 */
class RecommendationCarouselDataModel(
        val title: String,
        val appLinkSeeMore: String,
        val products: List<RecommendationCarouselItemDataModel>,
        val listener: RecommendationListener
) : HomeRecommendationDataModel {

    companion object{
        val LAYOUT = R.layout.fragment_recommendation_carousell
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)

    fun contains(item: RecommendationItem) = products.asSequence().any { it.productItem.productId == item.productId }

    fun contains(id: Int) = products.asSequence().any { it.productItem.productId == id }
}