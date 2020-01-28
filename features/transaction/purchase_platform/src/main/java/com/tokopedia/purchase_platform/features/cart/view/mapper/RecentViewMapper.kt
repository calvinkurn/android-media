package com.tokopedia.purchase_platform.features.cart.view.mapper

import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.RecentView
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewItemHolderData
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-12-11.
 */

class RecentViewMapper @Inject constructor() {

    fun convertToViewHolderModelList(recentViews: List<RecentView>): ArrayList<CartRecentViewItemHolderData> {
        val cartRecentViewItemHolderDataList = ArrayList<CartRecentViewItemHolderData>()
        recentViews.forEach {
            cartRecentViewItemHolderDataList.add(convertToViewHolderModel(it))
        }

        return cartRecentViewItemHolderDataList
    }

    private fun convertToViewHolderModel(recentView: RecentView): CartRecentViewItemHolderData {
        val cartRecentViewItemHolderData = CartRecentViewItemHolderData()
        cartRecentViewItemHolderData.id = recentView.productId ?: ""
        cartRecentViewItemHolderData.name = recentView.productName ?: ""
        cartRecentViewItemHolderData.price = recentView.productPrice ?: ""
        cartRecentViewItemHolderData.imageUrl = recentView.productImage ?: ""
        cartRecentViewItemHolderData.isWishlist = recentView.isWishlist
        cartRecentViewItemHolderData.rating = recentView.productRating
        cartRecentViewItemHolderData.reviewCount = recentView.productReviewCount
        cartRecentViewItemHolderData.shopLocation = recentView.shopLocation ?: ""
        cartRecentViewItemHolderData.shopId = recentView.shopId ?: ""
        cartRecentViewItemHolderData.shopName = recentView.shopName ?: ""
        cartRecentViewItemHolderData.minOrder = 1
        if (recentView.badges.size > 0) {
            cartRecentViewItemHolderData.badgeUrl = recentView.badges.get(0).imageUrl
            if (recentView.badges[0].title.equals("Official Store", ignoreCase = true)) {
                cartRecentViewItemHolderData.shopType = "official_store"
            } else if (recentView.badges[0].title.equals("Power Badge", ignoreCase = true)) {
                cartRecentViewItemHolderData.shopType = "power_badge"
            }
        }

        return cartRecentViewItemHolderData
    }
}