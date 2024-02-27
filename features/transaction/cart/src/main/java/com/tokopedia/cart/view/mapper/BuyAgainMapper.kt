package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.view.uimodel.CartBuyAgainItemHolderData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

object BuyAgainMapper {

    private fun convertToViewHolderModel(item: RecommendationItem): CartBuyAgainItemHolderData {
        val cartBuyAgainItemHolderData = CartBuyAgainItemHolderData()
        cartBuyAgainItemHolderData.id = item.productId.toString()
        cartBuyAgainItemHolderData.name = item.name
        cartBuyAgainItemHolderData.price = item.price
        cartBuyAgainItemHolderData.imageUrl = item.imageUrl
        cartBuyAgainItemHolderData.isWishlist = item.isWishlist
        cartBuyAgainItemHolderData.rating = item.rating
        cartBuyAgainItemHolderData.reviewCount = item.countReview
        cartBuyAgainItemHolderData.shopLocation = item.location
        cartBuyAgainItemHolderData.shopId = item.shopId.toString()
        cartBuyAgainItemHolderData.shopName = item.shopName
        cartBuyAgainItemHolderData.minOrder = 1
        cartBuyAgainItemHolderData.isTopAds = item.isTopAds
        cartBuyAgainItemHolderData.discountPercentage = item.discountPercentage
        cartBuyAgainItemHolderData.freeOngkirImageUrl = item.freeOngkirImageUrl
        cartBuyAgainItemHolderData.isFreeOngkirActive = item.isFreeOngkirActive
        cartBuyAgainItemHolderData.labelGroupList = item.labelGroupList
        cartBuyAgainItemHolderData.slashedPrice = item.slashedPrice
        cartBuyAgainItemHolderData.clickUrl = item.clickUrl
        cartBuyAgainItemHolderData.trackerImageUrl = item.trackerImageUrl
        cartBuyAgainItemHolderData.recommendationType = item.recommendationType
        cartBuyAgainItemHolderData.categoryBreadcrumbs = item.categoryBreadcrumbs

        if (item.badges.isNotEmpty()) {
            cartBuyAgainItemHolderData.badgesUrl = item.badges.map { it.imageUrl }
            if (item.badges[0].imageUrl.equals("Official Store", ignoreCase = true)) {
                cartBuyAgainItemHolderData.shopType = "official_store"
            } else if (item.badges[0].imageUrl.equals("Power Badge", ignoreCase = true)) {
                cartBuyAgainItemHolderData.shopType = "power_badge"
            }
        }

        return cartBuyAgainItemHolderData
    }

    fun convertToViewHolderModelList(items: RecommendationWidget?): ArrayList<CartBuyAgainItemHolderData> {
        val cartRecentViewItemHolderDataList = ArrayList<CartBuyAgainItemHolderData>()
        items?.recommendationItemList?.forEach {
            cartRecentViewItemHolderDataList.add(convertToViewHolderModel(it))
        }

        return cartRecentViewItemHolderDataList
    }
}

