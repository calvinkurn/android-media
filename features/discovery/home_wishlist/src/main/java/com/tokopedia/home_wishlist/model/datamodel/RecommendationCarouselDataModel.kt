package com.tokopedia.home_wishlist.model.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class RecommendationCarouselDataModel (
        val id: String,
        val title: String,
        val seeMoreAppLink: String,
        val list: List<RecommendationCarouselItemDataModel>,
        var isBulkMode: Boolean = false
): WishlistDataModel {
    override fun equalsDataModel(dataModel: Visitable<*>): Boolean {
        if(dataModel.javaClass == this.javaClass){
            return this.getUniqueIdentity() == (dataModel as RecommendationCarouselDataModel).getUniqueIdentity() &&
                    dataModel.isBulkMode == isBulkMode
        }
        return false
    }

    override fun getUniqueIdentity(): Any {
        return id
    }

    override fun type(typeFactory: WishlistTypeFactory): Int {
        return typeFactory.type(this)
    }

}