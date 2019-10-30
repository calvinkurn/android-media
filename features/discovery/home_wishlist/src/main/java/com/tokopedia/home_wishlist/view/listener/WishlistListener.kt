package com.tokopedia.home_wishlist.view.listener

import com.tokopedia.home_wishlist.base.SmartListener
import com.tokopedia.home_wishlist.model.datamodel.WishlistDataModel

interface WishlistListener : SmartListener{
    fun onProductImpression(dataModel: WishlistDataModel)
    fun onProductClick(dataModel: WishlistDataModel, position: Int)
    fun onDeleteClick(dataModel: WishlistDataModel, adapterPosition: Int)
    fun onAddToCartClick(dataModel: WishlistDataModel, callback: (Boolean) -> Unit)
    fun onWishlistClick(dataModel: WishlistDataModel)
}