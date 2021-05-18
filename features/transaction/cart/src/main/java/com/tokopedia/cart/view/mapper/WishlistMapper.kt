package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist
import java.util.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-12-11.
 */

class WishlistMapper @Inject constructor() {

    fun convertToViewHolderModelList(wishlists: List<Wishlist>): ArrayList<CartWishlistItemHolderData> {
        val cartWishlistItemHolderDataList = ArrayList<CartWishlistItemHolderData>()
        wishlists.forEach {
            cartWishlistItemHolderDataList.add(convertToViewHolderModel(it))
        }

        return cartWishlistItemHolderDataList
    }

    private fun convertToViewHolderModel(wishlist: Wishlist): CartWishlistItemHolderData {
        val cartWishlistItemHolderData = CartWishlistItemHolderData()
        cartWishlistItemHolderData.id = wishlist.id
        cartWishlistItemHolderData.name = wishlist.name
        cartWishlistItemHolderData.rawPrice = wishlist.price
        cartWishlistItemHolderData.price = wishlist.priceFmt
        cartWishlistItemHolderData.imageUrl = wishlist.imageUrl
        cartWishlistItemHolderData.url = wishlist.url
        cartWishlistItemHolderData.isWishlist = true
        cartWishlistItemHolderData.rating = wishlist.rating
        cartWishlistItemHolderData.reviewCount = wishlist.reviewCount
        cartWishlistItemHolderData.minOrder = wishlist.minimumOrder
        cartWishlistItemHolderData.category = wishlist.getCategoryBreadcrumb()
        cartWishlistItemHolderData.freeShippingExtra = wishlist.freeOngkirExtra?.isActive == true
        cartWishlistItemHolderData.freeShipping = wishlist.freeOngkir?.isActive == true
        cartWishlistItemHolderData.freeShippingUrl = when {
            wishlist.freeOngkirExtra?.isActive == true -> wishlist.freeOngkirExtra?.imageUrl ?: ""
            wishlist.freeOngkir?.isActive == true -> wishlist.freeOngkir?.imageUrl ?: ""
            else -> ""
        }
        if (wishlist.shop != null) {
            cartWishlistItemHolderData.shopId = wishlist.shop.id
            cartWishlistItemHolderData.shopName = wishlist.shop.name
            var shopType = ""
            if (wishlist.shop.isOfficial) {
                shopType = "official_store"
            } else if (wishlist.shop.isGoldMerchant) {
                shopType = "gold_merchant"
            }
            cartWishlistItemHolderData.shopType = shopType
            cartWishlistItemHolderData.shopLocation = wishlist.shop.location
        }
        if (wishlist.getBadges().size > 0) {
            cartWishlistItemHolderData.badgeUrl = wishlist.getBadges().get(0).imageUrl
        }

        return cartWishlistItemHolderData
    }
}