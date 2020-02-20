package com.tokopedia.home_wishlist.model.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory

data class RecommendationCarouselDataModel (
        val id: String = "",
        val title: String = "",
        val seeMoreAppLink: String = "",
        val list: List<RecommendationCarouselItemDataModel> = listOf(),
        var isOnBulkRemoveProgress: Boolean = false
): WishlistDataModel {

    override fun equalsDataModel(dataModel: Visitable<*>): Boolean {
        if(dataModel is RecommendationCarouselDataModel){
            return dataModel.isOnBulkRemoveProgress == isOnBulkRemoveProgress && dataModel.list == list && title == dataModel.title
                    && dataModel.list.zip(list).all { (list1, list2) -> list1.equalsDataModel(list2) }
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