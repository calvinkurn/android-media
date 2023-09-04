package com.tokopedia.cartrevamp.view.mapper

import com.tokopedia.cartrevamp.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.wishlistcommon.data.response.GetWishlistV2Response
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Created by Irfan Khoirul on 2019-12-11.
 */

class WishlistMapper @Inject constructor() {

    fun convertToViewHolderModelListV2(wishlists: List<GetWishlistV2Response.Data.WishlistV2.Item>): ArrayList<CartWishlistItemHolderData> {
        val cartWishlistItemHolderDataList = ArrayList<CartWishlistItemHolderData>()
        wishlists.forEach {
            cartWishlistItemHolderDataList.add(convertToViewHolderModelV2(it))
        }

        return cartWishlistItemHolderDataList
    }

    private fun convertToViewHolderModelV2(wishlist: GetWishlistV2Response.Data.WishlistV2.Item): CartWishlistItemHolderData {
        val cartWishlistItemHolderData = CartWishlistItemHolderData()
        cartWishlistItemHolderData.id = wishlist.id
        cartWishlistItemHolderData.name = wishlist.name
        cartWishlistItemHolderData.rawPrice = wishlist.price
        cartWishlistItemHolderData.price = wishlist.priceFmt
        cartWishlistItemHolderData.imageUrl = wishlist.imageUrl
        cartWishlistItemHolderData.url = wishlist.url
        cartWishlistItemHolderData.isWishlist = true
        cartWishlistItemHolderData.rating = wishlist.rating.toFloatOrZero().roundToInt()
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
