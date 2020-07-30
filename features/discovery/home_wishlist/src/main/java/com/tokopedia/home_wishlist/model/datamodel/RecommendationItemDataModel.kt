package com.tokopedia.home_wishlist.model.datamodel

import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.smart_recycler_helper.SmartVisitable

data class RecommendationItemDataModel(
        val title: String,
        val recommendationItem: RecommendationItem
) : WishlistDataModel {
    override fun equalsDataModel(dataModel: SmartVisitable<*>): Boolean {
        if(dataModel is RecommendationItemDataModel){
            return recommendationItem.isWishlist == dataModel.recommendationItem.isWishlist
        }
        return false
    }

    override fun getUniqueIdentity(): Any = recommendationItem.productId

    override fun type(typeFactory: WishlistTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + recommendationItem.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecommendationItemDataModel

        if (title != other.title) return false
        if (recommendationItem != other.recommendationItem) return false

        return true
    }
}