package com.tokopedia.home_wishlist.model.datamodel

import android.os.Bundle
import com.tokopedia.smart_recycler_helper.SmartVisitable
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory

class LoadMoreDataModel : WishlistDataModel {

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