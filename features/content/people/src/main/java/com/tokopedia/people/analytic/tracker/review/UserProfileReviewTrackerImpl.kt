package com.tokopedia.people.analytic.tracker.review

import com.tokopedia.people.analytic.UserProfileAnalytics
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.track.builder.Tracker
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on June 07, 2023
 */
class UserProfileReviewTrackerImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val trackingQueue: TrackingQueue,
) : UserProfileReviewTracker {

    override fun clickReviewTab(userId: String, isSelf: Boolean) {
        sendGeneralClickTracker(
            eventAction = "click - review tab",
            eventLabel = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(isSelf)}",
            trackerId = "44098"
        )
    }

    override fun clickUserProfileSettings(userId: String) {
        sendGeneralClickTracker(
            eventAction = "click - gear icon",
            eventLabel = userId,
            trackerId = "44099"
        )
    }

    override fun clickReviewSettingsToggle(userId: String, isOn: Boolean) {
        sendGeneralClickTracker(
            eventAction = "click - toggle button",
            eventLabel = "$userId - ${UserProfileAnalytics.Function.isOnOrOff(isOn)}",
            trackerId = "44100"
        )
    }

    override fun clickReviewMedia(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productId: String
    ) {
        sendGeneralClickTracker(
            eventAction = "click - review media",
            eventLabel = generateCompleteEventAction(userId, feedbackId, isSelf, productId),
            trackerId = "44101"
        )
    }

    override fun clickLikeReview(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productId: String
    ) {
        sendGeneralClickTracker(
            eventAction = "click - like review",
            eventLabel = generateCompleteEventAction(userId, feedbackId, isSelf, productId),
            trackerId = "44102"
        )
    }

    override fun impressReviewCard(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productId: String,
        position: Int
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Constants.PROMO_VIEW,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = "impression - review",
                label = generateCompleteEventAction(userId, feedbackId, isSelf, productId),
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.ECOMMERCE to hashMapOf(
                    UserProfileAnalytics.Constants.PROMO_VIEW to hashMapOf(
                        UserProfileAnalytics.Constants.PROMOTIONS to listOf(
                            hashMapOf(
                                UserProfileAnalytics.Constants.CREATIVE to "",
                                UserProfileAnalytics.Constants.POSITION to position,
                                UserProfileAnalytics.Constants.ID to feedbackId,
                                UserProfileAnalytics.Constants.NAME to "/feed user profile - review"
                            )
                        )
                    )
                )
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "44103",
            )
        )
    }

    override fun openScreenEmptyOrHiddenReviewTab() {
        UserProfileAnalytics.Variable.analyticTracker.sendScreenAuthenticated(
            "/user profile - review tab",
            mapOf(
                UserProfileAnalytics.Constants.TRACKER_ID to "44104",
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris
            )
        )
    }

    override fun clickReviewSeeMoreDescription(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productId: String
    ) {
        sendGeneralClickTracker(
            eventAction = "click - review selengkapnya",
            eventLabel = generateCompleteEventAction(userId, feedbackId, isSelf, productId),
            trackerId = "44108"
        )
    }

    override fun clickReviewProductInfo(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productReview: UserReviewUiModel.Product,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Constants.PRODUCT_CLICK,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = "click - product on review",
                label = generateCompleteEventAction(userId, feedbackId, isSelf, productReview.productID),
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.ECOMMERCE to hashMapOf(
                    UserProfileAnalytics.Constants.CLICK to hashMapOf(
                        UserProfileAnalytics.Constants.PRODUCTS to listOf(
                            mapOf(
                                UserProfileAnalytics.Constants.ITEM_BRAND to "",
                                UserProfileAnalytics.Constants.ITEM_CATEOGRY to "",
                                UserProfileAnalytics.Constants.ITEM_NAME to productReview.productName,
                                UserProfileAnalytics.Constants.ITEM_ID to productReview.productID,
                                UserProfileAnalytics.Constants.ITEM_VARIANT to productReview.productVariant.variantName,
                            )
                        )
                    )
                )
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.ITEM_LIST to "/user profile - review tab",
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "44110",
            )
        )
    }

    private fun generateCompleteEventAction(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productId: String
    ): String {
        return "$feedbackId - $userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(isSelf)} - $productId"
    }

    private fun sendGeneralClickTracker(
        eventAction: String,
        eventLabel: String,
        trackerId: String,
    ) {
        Tracker.Builder()
            .setEvent(UserProfileAnalytics.Event.EVENT_CLICK_CONTENT)
            .setEventCategory(UserProfileAnalytics.Category.FEED_USER_PROFILE)
            .setEventAction(eventAction)
            .setEventLabel(eventLabel)
            .setBusinessUnit(UserProfileAnalytics.Constants.CONTENT)
            .setCurrentSite(UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE)
            .setUserId(userSession.userId)
            .setCustomProperty(UserProfileAnalytics.Constants.SESSION_IRIS, UserProfileAnalytics.Variable.sessionIris)
            .setCustomProperty(UserProfileAnalytics.Constants.TRACKER_ID, trackerId)
            .build()
            .send()
    }
}
