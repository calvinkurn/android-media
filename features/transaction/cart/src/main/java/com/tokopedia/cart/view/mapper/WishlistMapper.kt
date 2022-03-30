package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.wishlist.data.model.response.GetWishlistV2Response
import java.util.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-12-11.
 */

class WishlistMapper @Inject constructor() {

    fun convertToViewHolderModelList(wishlists: List<GetWishlistV2Response.WishlistV2.Item>): ArrayList<CartWishlistItemHolderData> {
        val cartWishlistItemHolderDataList = ArrayList<CartWishlistItemHolderData>()
        wishlists.forEach {
            cartWishlistItemHolderDataList.add(convertToViewHolderModel(it))
        }

        return cartWishlistItemHolderDataList
    }

    private fun convertToViewHolderModel(wishlist: GetWishlistV2Response.WishlistV2.Item): CartWishlistItemHolderData {
        val cartWishlistItemHolderData = CartWishlistItemHolderData()
        cartWishlistItemHolderData.id = wishlist.id
        cartWishlistItemHolderData.name = wishlist.name
        cartWishlistItemHolderData.rawPrice = wishlist.price
        cartWishlistItemHolderData.price = wishlist.priceFmt
        cartWishlistItemHolderData.imageUrl = wishlist.imageUrl
        cartWishlistItemHolderData.url = wishlist.url
        cartWishlistItemHolderData.isWishlist = true
        cartWishlistItemHolderData.rating = wishlist.rating.toIntOrZero()
        cartWishlistItemHolderData.minOrder = wishlist.minOrder.toIntOrZero()
        cartWishlistItemHolderData.freeShipping = wishlist.bebasOngkir.title.isNotEmpty()
        cartWishlistItemHolderData.freeShippingUrl = wishlist.bebasOngkir.imageUrl
        cartWishlistItemHolderData.shopId = wishlist.shop.id
        cartWishlistItemHolderData.shopName = wishlist.shop.name
        cartWishlistItemHolderData.shopLocation = wishlist.shop.location
        if (wishlist.badges.isNotEmpty()) {
            cartWishlistItemHolderData.badgeUrl = wishlist.badges[0].imageUrl
        }

        return cartWishlistItemHolderData
    }
}