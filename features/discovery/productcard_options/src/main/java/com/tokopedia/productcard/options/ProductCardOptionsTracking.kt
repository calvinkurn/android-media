package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*
import java.util.*

internal object ProductCardOptionsTracking {

    fun eventSuccessWishlistSearchResultProduct(screenName: String, wishlistTrackingModel: WishlistTrackingModel) {
        val eventTrackingMap: MutableMap<String, Any> = mutableMapOf()

        eventTrackingMap[EVENT] = Event.CLICK_WISHLIST
        eventTrackingMap[EVENT_CATEGORY] = screenName.toLowerCase()
        eventTrackingMap[EVENT_ACTION] = generateWishlistClickEventAction(wishlistTrackingModel.isAddWishlist, wishlistTrackingModel.isUserLoggedIn)
        eventTrackingMap[EVENT_LABEL] = generateWishlistClickEventLabel(wishlistTrackingModel.productId, wishlistTrackingModel.isTopAds, wishlistTrackingModel.keyword)

        TrackApp.getInstance().gtm.sendGeneralEvent(eventTrackingMap)
    }

    private fun generateWishlistClickEventAction(isAddWishlist: Boolean, isLoggedIn: Boolean): String {
        return (getAddOrRemoveWishlistAction(isAddWishlist)
                + " - "
                + Action.MODULE
                + " - "
                + getIsLoggedInWishlistAction(isLoggedIn))
    }

    private fun getAddOrRemoveWishlistAction(isAddWishlist: Boolean): String {
        return if (isAddWishlist) Action.ADD_WISHLIST else Action.REMOVE_WISHLIST
    }

    private fun getIsLoggedInWishlistAction(isLoggedIn: Boolean): String {
        return if (isLoggedIn) Action.LOGIN else Action.NON_LOGIN
    }

    private fun generateWishlistClickEventLabel(productId: String, isTopAds: Boolean, keyword: String): String {
        return (productId
                + " - "
                + getTopAdsOrGeneralLabel(isTopAds)
                + " - "
                + keyword)
    }

    private fun getTopAdsOrGeneralLabel(isTopAds: Boolean): String {
        return if (isTopAds) Label.TOPADS else Label.GENERAL
    }

    fun eventClickSeeSimilarProduct(event: String, screenName: String, keyword: String, productId: String) {
        val eventTrackingMap: MutableMap<String, Any> = mutableMapOf()

        eventTrackingMap[EVENT] = event
        eventTrackingMap[EVENT_CATEGORY] = screenName.toLowerCase()
        eventTrackingMap[EVENT_ACTION] = Action.CLICK_SIMILAR_BUTTON
        eventTrackingMap[EVENT_LABEL] = String.format(Label.KEYWORD_PRODUCT_ID, keyword, productId)

        TrackApp.getInstance().gtm.sendGeneralEvent(eventTrackingMap)
    }
}