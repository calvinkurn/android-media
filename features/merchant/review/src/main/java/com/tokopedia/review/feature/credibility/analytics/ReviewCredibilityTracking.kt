package com.tokopedia.review.feature.credibility.analytics

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityAchievementBoxUiModel
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityHeaderUiModel
import com.tokopedia.reviewcommon.constant.AnalyticConstant
import com.tokopedia.reviewcommon.extension.appendBusinessUnit
import com.tokopedia.reviewcommon.extension.appendCurrentSite
import com.tokopedia.reviewcommon.extension.appendGeneralEventData
import com.tokopedia.reviewcommon.extension.appendPageSource
import com.tokopedia.reviewcommon.extension.appendProductID
import com.tokopedia.reviewcommon.extension.appendProductId
import com.tokopedia.reviewcommon.extension.appendTrackerIdIfNotBlank
import com.tokopedia.reviewcommon.extension.appendUserId
import com.tokopedia.reviewcommon.extension.sendEnhancedEcommerce
import com.tokopedia.reviewcommon.extension.sendGeneralEvent

object ReviewCredibilityTracking {

    private fun Bundle.appendImpressAchievementPromotionsEE(
        achievements: List<ReviewCredibilityAchievementBoxUiModel.ReviewCredibilityAchievementUiModel>
    ): Bundle {
        val promotions = achievements.mapIndexed { index, reviewCredibilityAchievementUiModel ->
            Bundle().apply {
                putString(AnalyticConstant.KEY_EE_CREATIVE_NAME, "")
                putInt(AnalyticConstant.KEY_EE_CREATIVE_SLOT, index)
                putString(AnalyticConstant.KEY_EE_ITEM_ID, ReviewCredibilityTrackingConstant.ITEM_ID_ACHIEVEMENT_STICKER)
                putString(AnalyticConstant.KEY_EE_ITEM_NAME, reviewCredibilityAchievementUiModel.name)
            }
        }
        putParcelableArrayList(AnalyticConstant.KEY_EE_PROMOTIONS, ArrayList(promotions))
        return this
    }

    fun trackOnClickCTASelfCredibility(
        ctaValue: String, productId: String, viewerUserId: String
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            AnalyticConstant.EVENT_CLICK_PG,
            ReviewCredibilityTrackingConstant.EVENT_CATEGORY_PERSONAL_STATISTICS_BOTTOM_SHEET,
            ReviewCredibilityTrackingConstant.EVENT_ACTION_CLICK_CTA,
            String.format(ReviewCredibilityTrackingConstant.EVENT_LABEL_CLICK_PERSONAL_CTA, ctaValue)
        ).appendBusinessUnit(ReviewTrackingConstant.BUSINESS_UNIT)
            .appendCurrentSite(ReviewTrackingConstant.CURRENT_SITE)
            .appendUserId(viewerUserId)
            .appendProductId(productId)
            .appendTrackerIdIfNotBlank(ReviewCredibilityTrackingConstant.TRACKER_ID_CLICK_CTA_SELF)
            .sendGeneralEvent()
    }

    fun trackOnClickCTAOtherUserCredibility(
        ctaValue: String, productId: String, viewerUserId: String, reviewerUserId: String
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            AnalyticConstant.EVENT_CLICK_PG,
            ReviewCredibilityTrackingConstant.EVENT_CATEGORY_OTHERS_STATISTICS_BOTTOM_SHEET,
            ReviewCredibilityTrackingConstant.EVENT_ACTION_CLICK_CTA,
            String.format(ReviewCredibilityTrackingConstant.EVENT_LABEL_CLICK_OTHERS_CTA, ctaValue, reviewerUserId)
        ).appendBusinessUnit(ReviewTrackingConstant.BUSINESS_UNIT)
            .appendCurrentSite(ReviewTrackingConstant.CURRENT_SITE)
            .appendUserId(viewerUserId)
            .appendProductId(productId)
            .appendTrackerIdIfNotBlank(ReviewCredibilityTrackingConstant.TRACKER_ID_CLICK_CTA_OTHER)
            .sendGeneralEvent()
    }

    fun trackOnClickAchievementSticker(
        usersOwnCredibility: Boolean,
        name: String,
        viewerUserId: String,
        reviewerUserId: String,
        productID: String
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            AnalyticConstant.EVENT_CLICK_PG,
            if (usersOwnCredibility) {
                ReviewCredibilityTrackingConstant.EVENT_CATEGORY_PERSONAL_STATISTICS_BOTTOM_SHEET
            } else {
                ReviewCredibilityTrackingConstant.EVENT_CATEGORY_OTHERS_STATISTICS_BOTTOM_SHEET
            },
            ReviewCredibilityTrackingConstant.EVENT_ACTION_CLICK_ACHIEVEMENT_STICKER,
            if (usersOwnCredibility) {
                String.format(
                    ReviewCredibilityTrackingConstant.EVENT_LABEL_CLICK_PERSONAL_ACHIEVEMENT_STICKER,
                    name,
                    Int.ZERO
                )
            } else {
                String.format(
                    ReviewCredibilityTrackingConstant.EVENT_LABEL_CLICK_OTHERS_ACHIEVEMENT_STICKER,
                    name,
                    Int.ZERO,
                    reviewerUserId
                )
            }
        ).appendBusinessUnit(ReviewTrackingConstant.BUSINESS_UNIT)
            .appendCurrentSite(ReviewTrackingConstant.CURRENT_SITE)
            .appendUserId(viewerUserId)
            .appendProductId(productID)
            .appendTrackerIdIfNotBlank(
                if (usersOwnCredibility) {
                    ReviewCredibilityTrackingConstant.TRACKER_ID_CLICK_ACHIEVEMENT_STICKER_SELF
                } else {
                    ReviewCredibilityTrackingConstant.TRACKER_ID_CLICK_ACHIEVEMENT_STICKER_OTHER
                }
            ).sendGeneralEvent()
    }

    fun trackOnClickSeeMoreAchievement(
        usersOwnCredibility: Boolean,
        buttonText: String,
        viewerUserId: String,
        reviewerUserId: String,
        productID: String
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            AnalyticConstant.EVENT_CLICK_PG,
            if (usersOwnCredibility) {
                ReviewCredibilityTrackingConstant.EVENT_CATEGORY_PERSONAL_STATISTICS_BOTTOM_SHEET
            } else {
                ReviewCredibilityTrackingConstant.EVENT_CATEGORY_OTHERS_STATISTICS_BOTTOM_SHEET
            },
            ReviewCredibilityTrackingConstant.EVENT_ACTION_CLICK_SEE_MORE_ACHIEVEMENT,
            if (usersOwnCredibility) {
                String.format(
                    ReviewCredibilityTrackingConstant.EVENT_LABEL_CLICK_PERSONAL_SEE_MORE_ACHIEVEMENT,
                    buttonText
                )
            } else {
                String.format(
                    ReviewCredibilityTrackingConstant.EVENT_LABEL_CLICK_OTHERS_SEE_MORE_ACHIEVEMENT,
                    buttonText,
                    reviewerUserId
                )
            }
        ).appendBusinessUnit(ReviewTrackingConstant.BUSINESS_UNIT)
            .appendCurrentSite(ReviewTrackingConstant.CURRENT_SITE)
            .appendUserId(viewerUserId)
            .appendProductId(productID)
            .appendTrackerIdIfNotBlank(
                if (usersOwnCredibility) {
                    ReviewCredibilityTrackingConstant.TRACKER_ID_CLICK_SEE_MORE_ACHIEVEMENT_SELF
                } else {
                    ReviewCredibilityTrackingConstant.TRACKER_ID_CLICK_SEE_MORE_ACHIEVEMENT_OTHER
                }
            ).sendGeneralEvent()
    }

    fun trackImpressAchievementStickers(
        usersOwnCredibility: Boolean,
        achievements: List<ReviewCredibilityAchievementBoxUiModel.ReviewCredibilityAchievementUiModel>,
        viewerUserId: String,
        reviewerUserId: String,
        productID: String,
        source: String
    ) {
        Bundle().appendGeneralEventData(
            AnalyticConstant.EVENT_VIEW_ITEM,
            if (usersOwnCredibility) {
                ReviewCredibilityTrackingConstant.EVENT_CATEGORY_PERSONAL_STATISTICS_BOTTOM_SHEET
            } else {
                ReviewCredibilityTrackingConstant.EVENT_CATEGORY_OTHERS_STATISTICS_BOTTOM_SHEET
            },
            ReviewCredibilityTrackingConstant.EVENT_ACTION_IMPRESS_ACHIEVEMENT_STICKER,
            if (usersOwnCredibility) {
                ReviewCredibilityTrackingConstant.EVENT_LABEL_PERSONAL_IMPRESS_ACHIEVEMENT_STICKER
            } else {
                String.format(
                    ReviewCredibilityTrackingConstant.EVENT_LABEL_OTHER_IMPRESS_ACHIEVEMENT_STICKER,
                    reviewerUserId
                )
            }
        ).appendBusinessUnit(ReviewTrackingConstant.BUSINESS_UNIT)
            .appendCurrentSite(ReviewTrackingConstant.CURRENT_SITE)
            .appendUserId(viewerUserId)
            .appendProductID(productID)
            .appendPageSource(source)
            .appendTrackerIdIfNotBlank(
                if (usersOwnCredibility) {
                    ReviewCredibilityTrackingConstant.TRACKER_ID_IMPRESS_ACHIEVEMENT_STICKER_SELF
                } else {
                    ReviewCredibilityTrackingConstant.TRACKER_ID_IMPRESS_ACHIEVEMENT_STICKER_OTHER
                }
            )
            .appendImpressAchievementPromotionsEE(achievements)
            .sendEnhancedEcommerce(AnalyticConstant.EVENT_VIEW_ITEM)
    }

    fun trackClickSeeProfileButton(
        trackingData: ReviewCredibilityHeaderUiModel.TrackingData
    ) {
        val otherUserCredibility = trackingData.viewerUserId != trackingData.reviewerUserId
        if (otherUserCredibility) {
            mutableMapOf<String, Any>()
                .appendGeneralEventData(
                    eventName = AnalyticConstant.EVENT_CLICK_PG,
                    eventCategory = ReviewCredibilityTrackingConstant.EVENT_CATEGORY_OTHERS_STATISTICS_BOTTOM_SHEET,
                    eventAction = ReviewCredibilityTrackingConstant.EVENT_ACTION_CLICK_SEE_PROFILE_BUTTON,
                    eventLabel = String.format(
                        ReviewCredibilityTrackingConstant.EVENT_LABEL_CLICK_SEE_PROFILE_BUTTON,
                        trackingData.reviewerUserId
                    )
                )
                .appendUserId(trackingData.viewerUserId)
                .appendProductId(trackingData.productId)
                .appendBusinessUnit(ReviewTrackingConstant.BUSINESS_UNIT)
                .appendCurrentSite(ReviewTrackingConstant.CURRENT_SITE)
                .appendPageSource(trackingData.pageSource)
                .appendTrackerIdIfNotBlank(ReviewCredibilityTrackingConstant.TRACKER_ID_CLICK_SEE_PROFILE_BUTTON)
                .sendGeneralEvent()
        }
    }
}
