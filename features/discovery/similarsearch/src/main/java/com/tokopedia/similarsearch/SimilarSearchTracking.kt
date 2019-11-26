package com.tokopedia.similarsearch

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

internal object SimilarSearchTracking {

    fun trackEventImpressionSimilarProduct(productId: String, productsItem: List<Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.PRODUCT_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, Category.SIMILAR_PRODUCT,
                        TrackAppUtils.EVENT_ACTION, Action.IMPRESSION_PRODUCT,
                        TrackAppUtils.EVENT_LABEL, String.format(Label.PRODUCT_ID, productId),
                        ECOMMERCE, DataLayer.mapOf(
                            ECommerce.CURRENCY_CODE, ECommerce.IDR,
                            ECommerce.IMPRESSIONS, productsItem
                        )
                )
        )
    }

    fun trackEventClickSimilarProduct(productId: String, screenName: String, productItem: Any) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.PRODUCT_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, Category.SIMILAR_PRODUCT,
                        TrackAppUtils.EVENT_ACTION, Action.CLICK_SIMILAR_PRODUCT,
                        TrackAppUtils.EVENT_LABEL, String.format(Label.ORIGIN_PRODUCT_ID_SCREEN_NAME, productId, screenName),
                        ECOMMERCE, DataLayer.mapOf(
                            ECommerce.CLICK, DataLayer.mapOf(
                                ECommerce.ACTION_FIELD, DataLayer.mapOf(
                                    "list", "/similarproduct"
                                ),
                                ECommerce.PRODUCTS, DataLayer.listOf(productItem)
                            )
                        )
                )
        )
    }

    fun trackEventEmptyResultSimilarSearch(productId: String, screenName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                    TrackAppUtils.EVENT, Event.VIEW_SEARCH_RESULT,
                    TrackAppUtils.EVENT_CATEGORY, Category.SIMILAR_PRODUCT,
                    TrackAppUtils.EVENT_ACTION, Action.NO_SIMILAR_PRODUCT,
                    TrackAppUtils.EVENT_LABEL, String.format(Label.ORIGIN_PRODUCT_ID_SCREEN_NAME, productId, screenName)
                )
        )
    }

    fun trackEventSuccessWishlistSimilarProduct(wishlistTrackingModel: WishlistTrackingModel) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.CLICK_WISHLIST,
                        TrackAppUtils.EVENT_CATEGORY, Category.SIMILAR_PRODUCT,
                        TrackAppUtils.EVENT_ACTION, generateWishlistClickEventAction(wishlistTrackingModel.isAddWishlist, wishlistTrackingModel.isUserLoggedIn),
                        TrackAppUtils.EVENT_LABEL, generateWishlistClickEventLabel(wishlistTrackingModel.productId, wishlistTrackingModel.isTopAds, wishlistTrackingModel.keyword)
                )
        )
    }

    private fun generateWishlistClickEventAction(isAddWishlist: Boolean, isLoggedIn: Boolean): String {
        return "${getAddOrRemoveWishlistAction(isAddWishlist)} - ${Action.MODULE} - ${getIsLoggedInWishlistAction(isLoggedIn)}"
    }

    private fun getAddOrRemoveWishlistAction(isAddWishlist: Boolean): String {
        return if (isAddWishlist) Action.ADD_WISHLIST else Action.REMOVE_WISHLIST
    }

    private fun getIsLoggedInWishlistAction(isLoggedIn: Boolean): String {
        return if (isLoggedIn) Action.LOGIN else Action.NON_LOGIN
    }

    private fun generateWishlistClickEventLabel(productId: String, isTopAds: Boolean, keyword: String): String {
        return "$productId - ${getTopAdsOrGeneralLabel(isTopAds)} - $keyword"
    }

    private fun getTopAdsOrGeneralLabel(isTopAds: Boolean): String {
        return if (isTopAds) Label.TOPADS else Label.GENERAL
    }
}