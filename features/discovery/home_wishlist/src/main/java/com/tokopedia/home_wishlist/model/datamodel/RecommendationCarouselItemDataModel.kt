package com.tokopedia.home_wishlist.model.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class RecommendationCarouselItemDataModel (
        val recommendationItem: RecommendationItem
) : WishlistDataModel {
    override fun equalsDataModel(dataModel: Visitable<*>): Boolean {
        if(dataModel.javaClass == this.javaClass){
            return this.getUniqueIdentity() == (dataModel as WishlistDataModel).getUniqueIdentity()
        }
        return false
    }

    override fun getUniqueIdentity(): Any = recommendationItem

    override fun type(typeFactory: WishlistTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object{
        val LAYOUT = R.layout.layout_recommendation_carousel_item
    }
}