package com.tokopedia.search.result.product.wishlist

import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTrackingConstant.EVENT
import com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_ACTION
import com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_CATEGORY
import com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_LABEL
import com.tokopedia.track.TrackApp

object WishlistTracking {
    private const val CLICK_WISHLIST = "clickWishlist"
    private const val MODULE = "module"
    private const val ADD_WISHLIST = "add wishlist"
    private const val REMOVE_WISHLIST = "remove wishlist"
    private const val LOGIN = "login"
    private const val NON_LOGIN = "nonlogin"
    private const val TOPADS = "topads"
    private const val GENERAL = "general"

    @JvmStatic
    fun eventSuccessWishlistSearchResultProduct(wishlistTrackingModel: WishlistTrackingModel) {
        val eventTrackingMap: MutableMap<String, Any> = HashMap()

        eventTrackingMap[EVENT] = CLICK_WISHLIST
        eventTrackingMap[EVENT_CATEGORY] = SearchEventTracking.Category.SEARCH_RESULT
        eventTrackingMap[EVENT_ACTION] = generateWishlistClickEventAction(wishlistTrackingModel)
        eventTrackingMap[EVENT_LABEL] = generateWishlistClickEventLabel(wishlistTrackingModel)

        TrackApp.getInstance().gtm.sendGeneralEvent(eventTrackingMap)
    }

    private fun generateWishlistClickEventAction(wishlistTrackingModel: WishlistTrackingModel): String {
        val isAddWishlist = wishlistTrackingModel.isAddWishlist
        val isLoggedIn = wishlistTrackingModel.isUserLoggedIn

        return (getAddOrRemoveWishlistAction(isAddWishlist)
            + " - "
            + MODULE
            + " - "
            + getIsLoggedInWishlistAction(isLoggedIn))
    }

    private fun getAddOrRemoveWishlistAction(isAddWishlist: Boolean): String {
        return if (isAddWishlist) ADD_WISHLIST else REMOVE_WISHLIST
    }

    private fun getIsLoggedInWishlistAction(isLoggedIn: Boolean): String {
        return if (isLoggedIn) LOGIN else NON_LOGIN
    }

    private fun generateWishlistClickEventLabel(wishlistTrackingModel: WishlistTrackingModel): String {
        val productId = wishlistTrackingModel.productId
        val isTopAds = wishlistTrackingModel.isTopAds
        val keyword = wishlistTrackingModel.keyword

        return (productId
            + " - "
            + getTopAdsOrGeneralLabel(isTopAds)
            + " - "
            + keyword)
    }

    private fun getTopAdsOrGeneralLabel(isTopAds: Boolean): String {
        return if (isTopAds) TOPADS else GENERAL
    }
}
