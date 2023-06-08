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

    override fun clickReviewTab(isSelf: Boolean) {

    }

    override fun clickUserProfileSettings() {

    }

    override fun clickReviewSettingsToggle(isOn: Boolean) {

    }

    override fun clickReviewMedia(feedbackId: String, isSelf: Boolean, productId: String) {

    }

    override fun clickLikeReview(feedbackId: String, isSelf: Boolean, productId: String) {

    }

    override fun impressReviewCard(
        position: Int,
        feedbackId: String,
        isSelf: Boolean,
        productId: String
    ) {

    }

    override fun openScreenEmptyOrHiddenReviewTab() {

    }

    override fun clickReviewSeeMoreDescription(
        feedbackId: String,
        isSelf: Boolean,
        productId: String
    ) {

    }

    override fun clickReviewProductInfo(feedbackId: String, isSelf: Boolean, productId: String) {

    }
}
