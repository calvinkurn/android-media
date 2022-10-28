package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.EmptyStateWishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.OtherWishlistModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistModel

interface WishlistTypeFactory {
    fun type(wishlistModel: WishlistModel): Int
    fun type(otherWishlistModel: OtherWishlistModel): Int
    fun type(emptyStateWishlistDataModel: EmptyStateWishlistDataModel): Int
}