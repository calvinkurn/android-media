package com.tokopedia.home_wishlist.model.datamodel

import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory
import com.tokopedia.smart_recycler_helper.SmartVisitable

class RecommendationTitleDataModel(
        val title: String,
        val seeMoreAppLink: String
) : WishlistDataModel {
    override fun equalsDataModel(dataModel: SmartVisitable<*>): Boolean {
        if(dataModel is RecommendationTitleDataModel){
            return title == dataModel.title
        }
        return false
    }

    override fun getUniqueIdentity(): Any = title

    override fun type(typeFactory: WishlistTypeFactory): Int {
        return typeFactory.type(this)
    }
}