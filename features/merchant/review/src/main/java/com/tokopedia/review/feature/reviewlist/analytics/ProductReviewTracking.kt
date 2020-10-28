package com.tokopedia.review.feature.reviewlist.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

/*
Review Seller Revamp
Data layer docs
https://docs.google.com/spreadsheets/d/1V8oaqxfu7A-rUq9BtZKxqP0Gy2WHFumiGNzh5piX9TI/edit#gid=1022075175
 */

class ProductReviewTracking {

    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

    private val EVENT = "event"
    private val EVENT_CATEGORY = "eventCategory"
    private val EVENT_ACTION = "eventAction"
    private val EVENT_LABEL = "eventLabel"
    private val EVENT_CLICK_REVIEW = "clickReview"
    private val CLICK = "click"
    private val POSITION = "position"

    private val FIELD_ULASAN_RATING_PRODUCT = "ulasan - rating produk"
    private val FIELD_RATING_PRODUCT = "rating produk"
    private val FIELD_SCREEN_NAME = "screenName"
    private val FIELD_SHOP_ID = "shopId"
    private val FIELD_PRODUCT_ID = "productId"

    fun sendScreen(shopId: String) {
        val dataLayer = mutableMapOf<String, String>()
        dataLayer[FIELD_SCREEN_NAME] = FIELD_RATING_PRODUCT
        dataLayer[FIELD_SHOP_ID] = shopId
        tracker.sendScreenAuthenticated(FIELD_SCREEN_NAME, dataLayer)
    }

    fun eventClickTabRatingProduct(shopId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_ULASAN_RATING_PRODUCT,
                EVENT_ACTION, "$CLICK - rating produk tab",
                EVENT_LABEL, "",
                FIELD_SHOP_ID, shopId,
                FIELD_SCREEN_NAME, FIELD_RATING_PRODUCT
        ))
    }

    fun eventScrollRatingProduct(shopId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_ULASAN_RATING_PRODUCT,
                EVENT_ACTION, "scroll - rating product page",
                EVENT_LABEL, "",
                FIELD_SHOP_ID, shopId,
                FIELD_SCREEN_NAME, FIELD_RATING_PRODUCT
        ))
    }

    fun eventClickItemRatingProduct(shopId: String, productId: String, productPosition: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_ULASAN_RATING_PRODUCT,
                EVENT_ACTION, "$CLICK - product on rating product",
                EVENT_LABEL, "$FIELD_PRODUCT_ID:$productId",
                POSITION, productPosition,
                FIELD_SHOP_ID, shopId,
                FIELD_SCREEN_NAME, FIELD_RATING_PRODUCT
        ))
    }

    fun eventClickSortRatingProduct(shopId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_ULASAN_RATING_PRODUCT,
                EVENT_ACTION, "$CLICK - sort on rating product page",
                EVENT_LABEL, "",
                FIELD_SHOP_ID, shopId,
                FIELD_SCREEN_NAME, FIELD_RATING_PRODUCT
        ))
    }

    fun eventClickSortBottomSheet(shopId: String, sortSelected: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_ULASAN_RATING_PRODUCT,
                EVENT_ACTION, "$CLICK - select sort on bottomsheet",
                EVENT_LABEL, "sortBy:$sortSelected",
                FIELD_SHOP_ID, shopId,
                FIELD_SCREEN_NAME, FIELD_RATING_PRODUCT
        ))
    }

    fun eventClickFilterRatingProduct(shopId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_ULASAN_RATING_PRODUCT,
                EVENT_ACTION, "$CLICK - filter on rating product page",
                EVENT_LABEL, "",
                FIELD_SHOP_ID, shopId,
                FIELD_SCREEN_NAME, FIELD_RATING_PRODUCT
        ))
    }

    fun eventClickFilterBottomSheet(shopId: String, filterSelected: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_ULASAN_RATING_PRODUCT,
                EVENT_ACTION, "$CLICK - select filter on bottomsheet",
                EVENT_LABEL, "filterBy:$filterSelected",
                FIELD_SHOP_ID, shopId,
                FIELD_SCREEN_NAME, FIELD_RATING_PRODUCT
        ))
    }

    fun eventClickRetryError(shopId: String, errorMessage: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_ULASAN_RATING_PRODUCT,
                EVENT_ACTION, "$CLICK - coba lagi on error messages",
                EVENT_LABEL, "message:$errorMessage",
                FIELD_SHOP_ID, shopId,
                FIELD_SCREEN_NAME, FIELD_RATING_PRODUCT
        ))
    }

    fun eventViewErrorIris(errorMessage: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, "viewReviewIris",
                EVENT_CATEGORY, FIELD_ULASAN_RATING_PRODUCT,
                EVENT_ACTION, "view error messages",
                EVENT_LABEL, "message:$errorMessage",
                FIELD_SCREEN_NAME, FIELD_RATING_PRODUCT
        ))
    }

    fun eventClickSearchBar(shopId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_ULASAN_RATING_PRODUCT,
                EVENT_ACTION, "$CLICK - search bar on rating product page",
                EVENT_LABEL, "",
                FIELD_SHOP_ID, shopId,
                FIELD_SCREEN_NAME, FIELD_RATING_PRODUCT
        ))
    }

    fun eventSubmitSearchBar(shopId: String, keyword: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_REVIEW,
                EVENT_CATEGORY, FIELD_ULASAN_RATING_PRODUCT,
                EVENT_ACTION, "$CLICK - search by keyword on rating product page",
                EVENT_LABEL, "keyword:$keyword",
                FIELD_SHOP_ID, shopId,
                FIELD_SCREEN_NAME, FIELD_RATING_PRODUCT
        ))
    }

}