package com.tokopedia.people.analytic.tracker.review

import com.tokopedia.trackingoptimizer.TrackingQueue
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

    }

    override fun clickUserProfileSettings(userId: String) {

    }

    override fun clickReviewSettingsToggle(userId: String, isOn: Boolean) {

    }

    override fun clickReviewMedia(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productId: String
    ) {

    }

    override fun clickLikeReview(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productId: String
    ) {

    }

    override fun impressReviewCard(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productId: String,
        position: Int
    ) {

    }

    override fun openScreenEmptyOrHiddenReviewTab() {

    }

    override fun clickReviewSeeMoreDescription(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productId: String
    ) {

    }

    override fun clickReviewProductInfo(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productId: String
    ) {

    }
}
