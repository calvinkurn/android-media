package com.tokopedia.review.feature.reading.analytics

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.feature.reading.analytics.ReadReviewTrackingConstants.SCREEN_NAME_SHOP_REVIEW
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object ReadReviewTracking {

    fun trackOpenScreen(screenName: String, productId: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, getOpenScreenCustomDimensMap(productId))
    }

    fun trackOnClickPositiveReviewPercentage(
        positiveReview: String,
        rating: Long,
        review: Long,
        productId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_POSITIVE_REVIEW_PERCENTAGE,
                String.format(
                    ReadReviewTrackingConstants.EVENT_LABEL_CLICK_POSITIVE_REVIEW_PERCENTAGE,
                    getPercentPositiveReview(positiveReview),
                    rating,
                    review
                ),
                productId
            )
        )
    }

    fun trackOnItemImpressed(
        feedbackId: String,
        position: Int,
        userId: String,
        countRating: Long,
        countReview: Long,
        characterCount: Int,
        imageCount: Int,
        productId: String,
        trackingQueue: TrackingQueue
    ) {
        val trackingMap = DataLayer.mapOf(
            ReviewTrackingConstant.EVENT,
            ReadReviewTrackingConstants.EVENT_PROMO_VIEW,
            ReviewTrackingConstant.EVENT_ACTION,
            ReadReviewTrackingConstants.EVENT_ACTION_IMPRESS_ITEM,
            ReviewTrackingConstant.EVENT_LABEL,
            String.format(
                ReadReviewTrackingConstants.EVENT_LABEL_IMPRESSION,
                countRating,
                countReview
            ),
            ReadReviewTrackingConstants.KEY_PRODUCT_ID,
            productId,
            ReviewTrackingConstant.EVENT_CATEGORY,
            ReadReviewTrackingConstants.EVENT_CATEGORY,
            ReadReviewTrackingConstants.KEY_USER_ID,
            userId,
            ReadReviewTrackingConstants.KEY_BUSINESS_UNIT,
            ReadReviewTrackingConstants.BUSINESS_UNIT,
            ReadReviewTrackingConstants.KEY_CURRENT_SITE,
            ReadReviewTrackingConstants.CURRENT_SITE,
            ReadReviewTrackingConstants.KEY_ECOMMERCE,
            DataLayer.mapOf(
                ReadReviewTrackingConstants.EVENT_PROMO_VIEW, DataLayer.mapOf(
                    ReadReviewTrackingConstants.KEY_PROMOTIONS, DataLayer.listOf(
                        DataLayer.mapOf(
                            ReadReviewTrackingConstants.KEY_ID,
                            feedbackId,
                            ReadReviewTrackingConstants.KEY_CREATIVE,
                            ReadReviewTrackingConstants.EVENT_CATEGORY,
                            ReadReviewTrackingConstants.KEY_NAME,
                            String.format(
                                ReadReviewTrackingConstants.EE_NAME,
                                characterCount,
                                imageCount
                            ),
                            ReadReviewTrackingConstants.KEY_POSITION,
                            position.toString(),
                            ReadReviewTrackingConstants.KEY_PRODUCT_ID,
                            productId
                        )
                    )
                )
            )
        )
        trackingQueue.putEETracking(trackingMap as HashMap<String, Any>?)
    }

    fun trackOnFilterClicked(filterName: String, isActive: Boolean, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_FILTER,
                String.format(
                    ReadReviewTrackingConstants.EVENT_LABEL_CLICK_FILTER,
                    filterName,
                    (!isActive).toString()
                ),
                productId
            )
        )
    }

    fun trackOnApplyFilterClicked(filterName: String, filterValue: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_APPLY_FILTER,
                String.format(
                    ReadReviewTrackingConstants.EVENT_LABEL_CLICK_APPLY_FILTER,
                    filterName,
                    filterValue
                ),
                productId
            )
        )
    }

    fun trackOnApplySortClicked(sortValue: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_APPLY_SORT,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_APPLY_SORT, sortValue),
                productId
            )
        )
    }

    fun trackOnSeeFullReviewClicked(feedbackId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_SEE_ALL,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_SEE_ALL, feedbackId),
                productId
            )
        )
    }

    fun trackOnLikeClicked(feedbackId: String, isLiked: Boolean, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_LIKE_REVIEW,
                String.format(
                    ReadReviewTrackingConstants.EVENT_LABEL_CLICK_LIKE_REVIEW,
                    feedbackId,
                    (!isLiked).toString()
                ),
                productId
            )
        )
    }

    fun trackOnSeeReplyClicked(feedbackId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_SEE_REPLY,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_SEE_REPLY, feedbackId),
                productId
            )
        )
    }

    fun trackOnImageClicked(feedbackId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_IMAGE,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_IMAGE, feedbackId),
                productId
            )
        )
    }

    fun trackOnReportClicked(feedbackId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_REPORT_REVIEW,
                String.format(
                    ReadReviewTrackingConstants.EVENT_LABEL_CLICK_REPORT_REVIEW,
                    feedbackId
                ),
                productId
            )
        )
    }

    fun trackOnClearFilter(productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_CLEAR_FILTER,
                "",
                productId
            )
        )
    }

    fun trackOnClickTopicRating(
        topic: String,
        topicPosition: Int,
        userId: String,
        productId: String
    ) {
        val trackingBundle = Bundle()
        trackingBundle.apply {
            putString(
                ReviewTrackingConstant.EVENT_ACTION,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_TOPIC_RATING
            )
            putString(
                ReviewTrackingConstant.EVENT_CATEGORY,
                ReadReviewTrackingConstants.EVENT_CATEGORY
            )
            putString(ReviewTrackingConstant.EVENT_LABEL, "")
            putString(ReadReviewTrackingConstants.KEY_USER_ID, userId)
            putString(
                ReadReviewTrackingConstants.KEY_BUSINESS_UNIT,
                ReadReviewTrackingConstants.BUSINESS_UNIT
            )
            putString(
                ReadReviewTrackingConstants.KEY_CURRENT_SITE,
                ReadReviewTrackingConstants.CURRENT_SITE
            )
            putString(ReadReviewTrackingConstants.KEY_PRODUCT_ID, productId)
            val bundleEcommerce = Bundle()
            bundleEcommerce.apply {
                val bundlePromo = Bundle().apply {
                    putString(ReadReviewTrackingConstants.KEY_CREATIVE, "")
                    putString(ReadReviewTrackingConstants.KEY_ID, topic)
                    putString(ReadReviewTrackingConstants.KEY_NAME, "")
                    putInt(ReadReviewTrackingConstants.KEY_POSITION, topicPosition)
                }
                val bundlePromoClick = Bundle().apply {
                    putParcelableArrayList(
                        ReadReviewTrackingConstants.KEY_PROMOTIONS,
                        mutableListOf(bundlePromo) as ArrayList<Bundle>
                    )
                }
                putBundle(ReadReviewTrackingConstants.KEY_PROMO_CLICK, bundlePromoClick)
            }
            putBundle(ReadReviewTrackingConstants.KEY_ECOMMERCE, bundleEcommerce)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            ReadReviewTrackingConstants.KEY_PROMO_CLICK,
            trackingBundle
        )
    }

    fun trackOnImpressHighlightedTopic(
        topic: String,
        topicPosition: Int,
        userId: String,
        productId: String
    ) {
        val trackingBundle = Bundle()
        trackingBundle.apply {
            putString(
                ReviewTrackingConstant.EVENT_ACTION,
                ReadReviewTrackingConstants.EVENT_ACTION_IMPRESS_TOPIC_RATING
            )
            putString(
                ReviewTrackingConstant.EVENT_CATEGORY,
                ReadReviewTrackingConstants.EVENT_CATEGORY
            )
            putString(ReviewTrackingConstant.EVENT_LABEL, "")
            putString(ReadReviewTrackingConstants.KEY_USER_ID, userId)
            putString(
                ReadReviewTrackingConstants.KEY_BUSINESS_UNIT,
                ReadReviewTrackingConstants.BUSINESS_UNIT
            )
            putString(
                ReadReviewTrackingConstants.KEY_CURRENT_SITE,
                ReadReviewTrackingConstants.CURRENT_SITE
            )
            putString(ReadReviewTrackingConstants.KEY_PRODUCT_ID, productId)
            val bundleEcommerce = Bundle()
            bundleEcommerce.apply {
                val bundlePromo = Bundle().apply {
                    putString(ReadReviewTrackingConstants.KEY_CREATIVE, "")
                    putString(ReadReviewTrackingConstants.KEY_ID, topic)
                    putString(ReadReviewTrackingConstants.KEY_NAME, "")
                    putInt(ReadReviewTrackingConstants.KEY_POSITION, topicPosition)
                }
                val bundlePromoClick = Bundle().apply {
                    putParcelableArrayList(
                        ReadReviewTrackingConstants.KEY_PROMOTIONS,
                        mutableListOf(bundlePromo) as ArrayList<Bundle>
                    )
                }
                putBundle(ReadReviewTrackingConstants.EVENT_PROMO_VIEW, bundlePromoClick)
            }
            putBundle(ReadReviewTrackingConstants.KEY_ECOMMERCE, bundleEcommerce)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            ReadReviewTrackingConstants.EVENT_PROMO_VIEW,
            trackingBundle
        )
    }

    private fun getPercentPositiveReview(percentPositiveFormatted: String): String {
        return percentPositiveFormatted.substringBefore("%")
    }

    private fun getOpenScreenCustomDimensMap(productId: String): Map<String, String> {
        return mapOf(
            ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.BUSINESS_UNIT,
            ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
            ReadReviewTrackingConstants.KEY_PRODUCT_ID to productId
        )
    }

    private fun getTrackEventMap(
        event: String,
        eventAction: String,
        eventLabel: String,
        productId: String
    ): Map<String, String> {
        return mapOf(
            ReviewTrackingConstant.EVENT to event,
            ReviewTrackingConstant.EVENT_ACTION to eventAction,
            ReviewTrackingConstant.EVENT_CATEGORY to ReadReviewTrackingConstants.EVENT_CATEGORY,
            ReviewTrackingConstant.EVENT_LABEL to eventLabel,
            ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.BUSINESS_UNIT,
            ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
            ReadReviewTrackingConstants.KEY_PRODUCT_ID to productId
        )
    }

    fun trackOpenScreenShopReview(shopId: String) {
        val screenName = String.format(SCREEN_NAME_SHOP_REVIEW, shopId)
        val shopReviewCustomDimension = getShopReviewCustomDimension(shopId).toMutableMap().apply {
            put(
                ReadReviewTrackingConstants.KEY_BUSINESS_UNIT,
                ReadReviewTrackingConstants.PHYSICAL_GOODS
            )
            put(
                ReadReviewTrackingConstants.KEY_CURRENT_SITE,
                ReadReviewTrackingConstants.CURRENT_SITE
            )
        }
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, shopReviewCustomDimension)
    }

    private fun getShopReviewCustomDimension(shopId: String): Map<String, String> {
        return mapOf(
            ReadReviewTrackingConstants.KEY_SHOP_ID to shopId
        )
    }


    fun trackOnShopReviewLikeClicked(feedbackId: String, isLiked: Boolean, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_SHOP_PAGE,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_LIKE_REVIEW,
                String.format(
                    ReadReviewTrackingConstants.EVENT_LABEL_CLICK_LIKE_REVIEW,
                    feedbackId,
                    (!isLiked).toString()
                ),
                shopId
            )
        )
    }

    fun trackOnShopReviewSeeReplyClicked(feedbackId: String, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_SHOP_PAGE,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_SEE_REPLY,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_SEE_REPLY, feedbackId),
                shopId
            )
        )
    }

    fun trackOnClickShopPositiveReviewPercentage(
        positiveReview: String,
        rating: Long,
        review: Long,
        shopId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_SHOP_PAGE,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_POSITIVE_REVIEW_PERCENTAGE,
                String.format(
                    ReadReviewTrackingConstants.EVENT_LABEL_CLICK_POSITIVE_REVIEW_PERCENTAGE,
                    getPercentPositiveReview(positiveReview),
                    rating,
                    review
                ),
                shopId
            )
        )
    }


    fun trackOnShopReviewImageClicked(feedbackId: String, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_SHOP_PAGE,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_IMAGE,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_IMAGE, feedbackId),
                shopId
            )
        )
    }

    fun trackOnShopReviewReportClicked(feedbackId: String, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_SHOP_PAGE,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_REPORT_REVIEW,
                String.format(
                    ReadReviewTrackingConstants.EVENT_LABEL_CLICK_REPORT_REVIEW,
                    feedbackId
                ),
                shopId
            )
        )
    }

    fun trackOnShopReviewClearFilter(shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_SHOP_PAGE,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_CLEAR_FILTER,
                "",
                shopId
            )
        )
    }

    private fun getShopReviewTrackEventMap(
        event: String,
        eventAction: String,
        eventLabel: String,
        shopId: String
    ): Map<String, String> {
        return mapOf(
            ReviewTrackingConstant.EVENT to event,
            ReviewTrackingConstant.EVENT_ACTION to eventAction,
            ReviewTrackingConstant.EVENT_CATEGORY to ReadReviewTrackingConstants.EVENT_CATEGORY_SHOP_REVIEW,
            ReviewTrackingConstant.EVENT_LABEL to eventLabel,
            ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.PHYSICAL_GOODS,
            ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
            ReadReviewTrackingConstants.KEY_SHOP_ID to shopId
        )
    }

    fun trackOnFilterShopReviewClicked(filterName: String, isActive: Boolean, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_SHOP_PAGE,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_FILTER,
                String.format(
                    ReadReviewTrackingConstants.EVENT_LABEL_CLICK_FILTER,
                    filterName,
                    (!isActive).toString()
                ),
                shopId
            )
        )
    }

    fun trackOnShopReviewApplyRatingFilter(
        filterName: String,
        filterValue: String,
        shopId: String
    ) {
        val isFilterActive = filterValue.isNotEmpty()
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_SHOP_PAGE,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_APPLY_FILTER,
                String.format(
                    ReadReviewTrackingConstants.EVENT_LABEL_CLICK_SHOP_APPLY_FILTER_RATING,
                    filterName,
                    isFilterActive.toString()
                ),
                shopId
            )
        )
    }

    fun trackOnShopReviewApplyTopicFilter(filterName: String, filterValue: String, shopId: String) {
        val isFilterActive = filterValue.isNotEmpty()
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_SHOP_PAGE,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_APPLY_FILTER,
                String.format(
                    ReadReviewTrackingConstants.EVENT_LABEL_CLICK_SHOP_APPLY_FILTER_TOPIC,
                    filterName,
                    filterValue,
                    isFilterActive.toString()
                ),
                shopId
            )
        )
    }

    fun trackOnShopReviewApplySortClicked(sortValue: String, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_SHOP_PAGE,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_APPLY_SORT,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_APPLY_SORT, sortValue),
                shopId
            )
        )
    }

    fun trackOnShopReviewItemImpressed(
        feedbackId: String,
        position: Int,
        userId: String,
        countRating: Long,
        countReview: Long,
        characterCount: Int,
        imageCount: Int,
        shopId: String,
        trackingQueue: TrackingQueue
    ) {
        trackingQueue.putEETracking(
            hashMapOf(
                ReviewTrackingConstant.EVENT to ReadReviewTrackingConstants.EVENT_PROMO_VIEW,
                ReviewTrackingConstant.EVENT_ACTION to ReadReviewTrackingConstants.EVENT_ACTION_IMPRESS_ITEM,
                ReviewTrackingConstant.EVENT_LABEL to String.format(
                    ReadReviewTrackingConstants.EVENT_LABEL_IMPRESSION,
                    countRating,
                    countReview
                ),
                ReviewTrackingConstant.EVENT_CATEGORY to ReadReviewTrackingConstants.EVENT_CATEGORY_SHOP_REVIEW,
                ReadReviewTrackingConstants.KEY_USER_ID to userId,
                ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.PHYSICAL_GOODS,
                ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
                ReadReviewTrackingConstants.KEY_SHOP_ID to shopId,
                ReadReviewTrackingConstants.KEY_ECOMMERCE to mapOf(
                    ReadReviewTrackingConstants.EVENT_PROMO_VIEW to mapOf(
                        ReadReviewTrackingConstants.KEY_PROMOTIONS to listOf(
                            mapOf(
                                ReadReviewTrackingConstants.KEY_ID to feedbackId,
                                ReadReviewTrackingConstants.KEY_CREATIVE to "",
                                ReadReviewTrackingConstants.KEY_NAME to String.format(
                                    ReadReviewTrackingConstants.EE_NAME,
                                    characterCount,
                                    imageCount
                                ),
                                ReadReviewTrackingConstants.KEY_POSITION to position.toString()
                            )
                        )
                    )
                )
            )
        )
    }

    fun trackOnShopReviewSeeFullReviewClicked(feedbackId: String, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_SHOP_PAGE,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_SEE_ALL,
                String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_SEE_ALL, feedbackId),
                shopId
            )
        )
    }

    fun trackOnClickProductInfo(
        reviewId: String,
        shopName: String,
        productName: String,
        position: Int,
        productPrice: String, shopId: String, productId: String, userId: String,
        trackingQueue: TrackingQueue
    ) {
        val eventLabel = String.format(
            ReadReviewTrackingConstants.EVENT_LABEL_CLICK_PRODUCT_ON_REVIEW_CARD,
            reviewId
        )
        val trackerEventMap = hashMapOf(
            ReviewTrackingConstant.EVENT to ReadReviewTrackingConstants.EVENT_PRODUCT_CLICK,
            ReviewTrackingConstant.EVENT_ACTION to ReadReviewTrackingConstants.EVENT_ACTION_PRODUCT_CLICK,
            ReviewTrackingConstant.EVENT_CATEGORY to ReadReviewTrackingConstants.EVENT_CATEGORY_SHOP_REVIEW,
            ReviewTrackingConstant.EVENT_LABEL to eventLabel,
            ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.PHYSICAL_GOODS,
            ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
            ReadReviewTrackingConstants.KEY_ECOMMERCE to mapOf(
                ReadReviewTrackingConstants.CLICK to mapOf(
                    ReadReviewTrackingConstants.ACTION_FIELD to mapOf(
                        ReadReviewTrackingConstants.LIST to ""
                    ),
                    ReadReviewTrackingConstants.PRODUCTS to listOf(
                        mapOf(
                            ReadReviewTrackingConstants.BRAND to shopName,
                            ReadReviewTrackingConstants.CATEGORY to ReadReviewTrackingConstants.NONE,
                            ReadReviewTrackingConstants.KEY_ID to productId,
                            ReadReviewTrackingConstants.KEY_NAME to productName,
                            ReadReviewTrackingConstants.KEY_POSITION to position,
                            ReadReviewTrackingConstants.PRICE to productPrice,
                            ReadReviewTrackingConstants.VARIANT to ReadReviewTrackingConstants.NONE
                        )
                    )
                )
            ),
            ReadReviewTrackingConstants.KEY_PRODUCT_ID to productId,
            ReadReviewTrackingConstants.KEY_SHOP_ID to shopId,
            ReadReviewTrackingConstants.KEY_USER_ID to userId
        )
        trackingQueue.putEETracking(trackerEventMap)
    }

    fun trackOnClickProductInfoThreeDots(reviewId: String, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getShopReviewTrackEventMap(
                ReadReviewTrackingConstants.EVENT_CLICK_SHOP_PAGE,
                ReadReviewTrackingConstants.EVENT_ACTION_CLICK_PRODUCT_INFO_THREE_DOTS,
                String.format(
                    ReadReviewTrackingConstants.EVENT_LABEL_CLICK_PRODUCT_INFO_THREE_DOTS,
                    reviewId
                ),
                shopId
            )
        )
    }

    fun trackOnGoToCredibility(feedbackId: String, userId: String, statistics: String, productId: String, currentUserId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                ReviewTrackingConstant.EVENT to ReadReviewTrackingConstants.EVENT_CLICK_PDP,
                ReviewTrackingConstant.EVENT_ACTION to ReadReviewTrackingConstants.EVENT_ACTION_CLICK_USER_NAME,
                ReviewTrackingConstant.EVENT_CATEGORY to ReadReviewTrackingConstants.EVENT_CATEGORY,
                ReviewTrackingConstant.EVENT_LABEL to String.format(ReadReviewTrackingConstants.EVENT_LABEL_CLICK_USER_NAME, feedbackId, userId, statistics),
                ReadReviewTrackingConstants.KEY_BUSINESS_UNIT to ReadReviewTrackingConstants.BUSINESS_UNIT,
                ReadReviewTrackingConstants.KEY_CURRENT_SITE to ReadReviewTrackingConstants.CURRENT_SITE,
                ReadReviewTrackingConstants.KEY_PRODUCT_ID to productId,
                ReadReviewTrackingConstants.KEY_USER_ID to currentUserId
            )
        )
    }
}

