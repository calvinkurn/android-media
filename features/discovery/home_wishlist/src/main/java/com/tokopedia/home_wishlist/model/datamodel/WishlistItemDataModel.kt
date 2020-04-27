package com.tokopedia.home_wishlist.model.datamodel

import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.smart_recycler_helper.SmartVisitable

data class WishlistItemDataModel(
        val productItem: WishlistItem,
        var isOnBulkRemoveProgress: Boolean = false,
        var isOnChecked: Boolean = false,
        var isOnAddToCartProgress: Boolean = false
) : WishlistDataModel, ImpressHolder(){

    override fun getUniqueIdentity(): Any = productItem.id

    override fun equalsDataModel(dataModel: SmartVisitable<*>): Boolean {
        if(dataModel is WishlistItemDataModel){
            return dataModel.isOnBulkRemoveProgress == isOnBulkRemoveProgress &&
                    dataModel.isOnChecked == isOnChecked &&
                    dataModel.isOnAddToCartProgress == isOnAddToCartProgress
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