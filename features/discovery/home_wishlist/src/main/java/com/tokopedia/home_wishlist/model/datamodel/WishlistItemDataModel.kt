package com.tokopedia.home_wishlist.model.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class WishlistItemDataModel(
        val productItem: WishlistItem,
        var isBulkMode: Boolean = false,
        var isChecked: Boolean = false,
        var isOnAddToCart: Boolean = false
) : WishlistDataModel{

    override fun getUniqueIdentity(): Any = productItem.id

    override fun equalsDataModel(dataModel: Visitable<*>): Boolean {
        if(dataModel is WishlistItemDataModel){
            return dataModel.isBulkMode == isBulkMode &&
                    dataModel.isChecked == isChecked &&
                    dataModel.isOnAddToCart == isOnAddToCart
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