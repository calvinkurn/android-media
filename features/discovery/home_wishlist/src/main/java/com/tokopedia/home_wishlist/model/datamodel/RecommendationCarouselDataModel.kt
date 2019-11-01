package com.tokopedia.home_wishlist.model.datamodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory
import com.tokopedia.home_wishlist.view.ext.equalsList
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class RecommendationCarouselDataModel (
        val id: String,
        val title: String,
        val seeMoreAppLink: String,
        val list: MutableList<RecommendationCarouselItemDataModel>,
        var isBulkMode: Boolean = false
): WishlistDataModel {

    override fun equalsDataModel(dataModel: Visitable<*>): Boolean {
        if(dataModel is RecommendationCarouselDataModel){
            return dataModel.isBulkMode == isBulkMode && dataModel.title == title
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