package com.tokopedia.home_wishlist.model.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory

class RecommendationCarouselDataModel (
        val title: String,
        val list: List<String>
): WishlistDataModel {
    override fun equalsDataModel(dataModel: Visitable<*>): Boolean {
        return this.getUniqueIdentity() == (dataModel as RecommendationCarouselDataModel).getUniqueIdentity()
    }

    override fun getUniqueIdentity(): Any {
        return title
    }

    override fun type(typeFactory: WishlistTypeFactory): Int {
        return typeFactory.type(this)
    }

}