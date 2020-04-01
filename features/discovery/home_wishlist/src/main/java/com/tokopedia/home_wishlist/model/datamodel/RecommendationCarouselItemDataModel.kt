package com.tokopedia.home_wishlist.model.datamodel

import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.smart_recycler_helper.SmartVisitable

data class RecommendationCarouselItemDataModel (
        val recommendationItem: RecommendationItem,
        val title: String,
        val parentPosition: Int
) : WishlistDataModel {
    override fun equalsDataModel(dataModel: SmartVisitable<*>): Boolean {
        if(dataModel is RecommendationCarouselItemDataModel){
            return recommendationItem.isWishlist == dataModel.recommendationItem.isWishlist && recommendationItem.productId == dataModel.recommendationItem.productId
        }
        return false
    }

    override fun getUniqueIdentity(): Any = recommendationItem.productId

    override fun type(typeFactory: WishlistTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object{
        val LAYOUT = R.layout.layout_recommendation_carousel_item
    }
}