package com.tokopedia.home_wishlist.model.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

class WishlistItemDataModel(
        val productItem: WishlistItem
) : WishlistDataModel{
    override fun getUniqueIdentity(): Any = productItem

    override fun equalsDataModel(dataModel: Visitable<*>): Boolean {
        if(dataModel.javaClass == this::javaClass){
            return this.getUniqueIdentity() == (dataModel as WishlistDataModel).getUniqueIdentity()
        }
        return false
    }

    override fun type(typeFactory: WishlistTypeFactory): Int {
        return typeFactory.type(this)
    }
    companion object{
        val LAYOUT = R.layout.layout_wishlist_item
    }
}