package com.tokopedia.home_wishlist.model.datamodel

import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory
import com.tokopedia.smart_recycler_helper.SmartVisitable

class EmptySearchWishlistDataModel(
        val keyword: String
): WishlistDataModel {
    override fun equalsDataModel(dataModel: SmartVisitable<*>): Boolean {
        if(dataModel.javaClass == this.javaClass){
            return this.getUniqueIdentity() == (dataModel as WishlistDataModel).getUniqueIdentity()
        }
        return false
    }

    override fun getUniqueIdentity(): Any = this.hashCode()

    override fun type(typeFactory: WishlistTypeFactory): Int {
        return typeFactory.type(this)
    }
}