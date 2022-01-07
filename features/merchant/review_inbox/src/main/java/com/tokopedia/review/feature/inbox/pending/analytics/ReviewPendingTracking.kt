package com.tokopedia.review.feature.inbox.pending.analytics

import android.os.Bundle
import com.tokopedia.review.common.analytics.ReviewInboxTrackingConstants
import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object ReviewPendingTracking {

    fun eventClickCard(reputationId: String, productId: String, userId: String, isEligible: Boolean, source: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        String.format(ReviewPendingTrackingConstants.EVENT_LABEL_INCENTIVE, reputationId, productId, isEligible.toString()),
                        ReviewPendingTrackingConstants.EVENT_ACTION_CLICK_PRODUCT_CARD,
                        userId,
                        source
                )
        )
    }

    fun eventClickRatingStar(reputationId: String, productId: String, starRating: Int, userId: String, isEligible: Boolean, source: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                generateTrackingMap(
                        String.format(ReviewPendingTrackingConstants.EVENT_LABEL_PENDING, reputationId, productId, isEligible.toString()),
                        String.format(ReviewPendingTrackingConstants.EVENT_ACTION_CLICK_STAR, starRating.toString()),
                        userId,
                        source
                )
        )
    }

    fun sendScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun trackCredibilityCarouselItemClick(position: Int, title: String, userId: String) {
        Bundle().appendGeneralEventData(
            eventName = ReviewPendingTrackingConstants.EVENT_NAME_VALUE_SELECT_CONTENT,
            eventCategory = ReviewPendingTrackingConstants.EVENT_CATEGORY_VALUE_REVIEW_PAGE_PENDING_REVIEW,
            eventAction = ReviewPendingTrackingConstants.EVENT_ACTION_VALUE_CLICK_WIDGET_ON_REVIEW_INBOX,
            eventLabel = String.format(ReviewPendingTrackingConstants.EVENT_LABEL_VALUE_CAROUSEL_ITEM, title)
        ).appendBusinessUnit(ReviewPendingTrackingConstants.PDP_BUSINESS_UNIT)
            .appendCurrentSite(ReviewPendingTrackingConstants.CREDIBILITY_CURRENT_SITE)
            .appendBannerPromotions(position, title)
            .appendUserId(userId)
            .sendEnhancedEcommerce(ReviewPendingTrackingConstants.EVENT_NAME_VALUE_SELECT_CONTENT)
    }

    fun trackCredibilityCarouselItemImpression(position: Int, title: String, userId: String) {
        Bundle().appendGeneralEventData(
            eventName = ReviewPendingTrackingConstants.EVENT_NAME_VALUE_VIEW_ITEM,
            eventCategory = ReviewPendingTrackingConstants.EVENT_CATEGORY_VALUE_REVIEW_PAGE_PENDING_REVIEW,
            eventAction = ReviewPendingTrackingConstants.EVENT_ACTION_VALUE_IMPRESSION_WIDGET_ON_REVIEW_INBOX,
            eventLabel = ""
        ).appendBusinessUnit(ReviewPendingTrackingConstants.PDP_BUSINESS_UNIT)
            .appendCurrentSite(ReviewPendingTrackingConstants.CREDIBILITY_CURRENT_SITE)
            .appendBannerPromotions(position, title)
            .appendUserId(userId)
            .sendEnhancedEcommerce(ReviewPendingTrackingConstants.EVENT_NAME_VALUE_VIEW_ITEM)
    }

    private fun generateTrackingMap(label: String, action: String, userId: String, source: String): Map<String, String> {
        with(ReviewTrackingConstant) {
            return mapOf(
                    EVENT to EVENT_CLICK_REVIEW,
                    EVENT_CATEGORY to ReviewInboxTrackingConstants.EVENT_CATEGORY_PENDING_TAB,
                    EVENT_ACTION to action,
                    EVENT_LABEL to label,
                    KEY_SCREEN_NAME to ReviewInboxTrackingConstants.SCREEN_NAME,
                    KEY_USER_ID to "$userId,",
                    KEY_PAGE_SOURCE to source
            )
        }
    }

    private fun Bundle.appendGeneralEventData(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String
    ): Bundle {
        putString(TrackAppUtils.EVENT, eventName)
        putString(TrackAppUtils.EVENT_CATEGORY, eventCategory)
        putString(TrackAppUtils.EVENT_ACTION, eventAction)
        putString(TrackAppUtils.EVENT_LABEL, eventLabel)
        return this
    }

    private fun Bundle.appendBusinessUnit(businessUnit: String): Bundle {
        putString(ReviewPendingTrackingConstants.BUSINESS_UNIT, businessUnit)
        return this
    }

    private fun Bundle.appendCurrentSite(currentSite: String): Bundle {
        putString(ReviewPendingTrackingConstants.CURRENT_SITE, currentSite)
        return this
    }

    private fun Bundle.appendUserId(userId: String): Bundle {
        putString(ReviewTrackingConstant.KEY_USER_ID, userId)
        return this
    }

    private fun Bundle.appendBannerPromotions(position: Int, title: String): Bundle {
        val bannersPayload = listOf(
            Bundle().apply {
                putString(ReviewPendingTrackingConstants.EVENT_FIELD_EE_CREATIVE_NAME, null)
                putInt(ReviewPendingTrackingConstants.EVENT_FIELD_EE_CREATIVE_SLOT, position)
                putString(ReviewPendingTrackingConstants.EVENT_FIELD_EE_ITEM_ID, title)
                putString(ReviewPendingTrackingConstants.EVENT_FIELD_EE_ITEM_NAME, null)
            }
        )
        putParcelableArrayList(ReviewPendingTrackingConstants.EVENT_FIELD_EE_PROMOTIONS, ArrayList(bannersPayload))
        return this
    }

    private fun Bundle.sendEnhancedEcommerce(eventName: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, this)
    }
}
