package com.tokopedia.home_wishlist.model.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class RecommendationItemDataModel(
        val title: String,
        val recommendationItem: RecommendationItem
) : WishlistDataModel {
    override fun equalsDataModel(dataModel: Visitable<*>): Boolean {
        if(dataModel is RecommendationItemDataModel){
            return recommendationItem.isWishlist == dataModel.recommendationItem.isWishlist
        }
        return false
    }

    override fun getUniqueIdentity(): Any = recommendationItem.productId

    override fun type(typeFactory: WishlistTypeFactory): Int {
        return typeFactory.type(this)
    }
}