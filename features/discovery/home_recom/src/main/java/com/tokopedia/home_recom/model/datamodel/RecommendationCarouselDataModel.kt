package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.home_recom.view.viewholder.RecommendationCarouselViewHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * A Class of DataModel.
 *
 * This class for holding data for type factory pattern [RecommendationCarouselViewHolder]
 * @param title the title of widget recommendation carousel
 * @param products the list of recommendation item, it hold data for carousel
 */
class RecommendationCarouselDataModel(
        val title: String,
        val appLinkSeeMore: String,
        val products: List<RecommendationCarouselItemDataModel>
) : HomeRecommendationDataModel {

    companion object{
        val LAYOUT = R.layout.fragment_recommendation_carousell
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)

    fun contains(item: RecommendationItem) = products.any { it.productItem.productId == item.productId }

    fun contains(id: Int) = products.any { it.productItem.productId == id }
}