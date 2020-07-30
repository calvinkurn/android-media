package com.tokopedia.home_wishlist.view.listener

import com.tokopedia.home_wishlist.model.datamodel.WishlistDataModel
import com.tokopedia.smart_recycler_helper.SmartListener

interface WishlistListener : SmartListener {
    fun onProductImpression(dataModel: WishlistDataModel, position: Int)
    fun onProductClick(dataModel: WishlistDataModel, parentPosition: Int, position: Int)
    fun onDeleteClick(dataModel: WishlistDataModel, adapterPosition: Int)
    fun onAddToCartClick(dataModel: WishlistDataModel, adapterPosition: Int)
    fun onWishlistClick(parentPosition: Int, childPosition: Int, wishlistStatus: Boolean)
    fun onClickCheckboxDeleteWishlist(position: Int, isChecked: Boolean)
    fun onTryAgainClick()
}