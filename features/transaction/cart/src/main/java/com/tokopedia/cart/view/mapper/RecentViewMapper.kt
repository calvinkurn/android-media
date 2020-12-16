package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-12-11.
 */

class RecentViewMapper @Inject constructor() {

    private fun convertToViewHolderModel(recentView: RecommendationItem): CartRecentViewItemHolderData {
        val cartRecentViewItemHolderData = CartRecentViewItemHolderData()
        cartRecentViewItemHolderData.id = recentView.productId.toString() ?: ""
        cartRecentViewItemHolderData.name = recentView.name ?: ""
        cartRecentViewItemHolderData.price = recentView.price ?: ""
        cartRecentViewItemHolderData.imageUrl = recentView.imageUrl ?: ""
        cartRecentViewItemHolderData.isWishlist = recentView.isWishlist
        cartRecentViewItemHolderData.rating = recentView.rating
        cartRecentViewItemHolderData.reviewCount = recentView.countReview
        cartRecentViewItemHolderData.shopLocation = recentView.location ?: ""
        cartRecentViewItemHolderData.shopId = recentView.shopId.toString() ?: ""
        cartRecentViewItemHolderData.shopName = recentView.shopName ?: ""
        cartRecentViewItemHolderData.minOrder = 1
        cartRecentViewItemHolderData.isTopAds = recentView.isTopAds
        cartRecentViewItemHolderData.discountPercentage = recentView.discountPercentage
        cartRecentViewItemHolderData.freeOngkirImageUrl = recentView.freeOngkirImageUrl
        cartRecentViewItemHolderData.isFreeOngkirActive = recentView.isFreeOngkirActive
        cartRecentViewItemHolderData.labelGroupList = recentView.labelGroupList
        cartRecentViewItemHolderData.slashedPrice = recentView.slashedPrice
        cartRecentViewItemHolderData.clickUrl = recentView.clickUrl
        cartRecentViewItemHolderData.trackerImageUrl = recentView.trackerImageUrl

        if (recentView.badgesUrl.isNotEmpty()) {
            cartRecentViewItemHolderData.badgesUrl = recentView.badgesUrl
            if (recentView.badgesUrl[0].equals("Official Store", ignoreCase = true)) {
                cartRecentViewItemHolderData.shopType = "official_store"
            } else if (recentView.badgesUrl[0].equals("Power Badge", ignoreCase = true)) {
                cartRecentViewItemHolderData.shopType = "power_badge"
            }
        }

        return cartRecentViewItemHolderData
    }

    fun convertToViewHolderModelList(recentViews: RecommendationWidget?): ArrayList<CartRecentViewItemHolderData> {
        val cartRecentViewItemHolderDataList = ArrayList<CartRecentViewItemHolderData>()
        recentViews?.recommendationItemList?.forEach {
            cartRecentViewItemHolderDataList.add(convertToViewHolderModel(it))
        }

        return cartRecentViewItemHolderDataList
    }
}