package com.tokopedia.people.analytic.tracker.review

import com.tokopedia.people.views.uimodel.UserReviewUiModel

/**
 * Created By : Jonathan Darwin on June 07, 2023
 */

/**
 * Mynakama Tracker
 * https://mynakama.tokopedia.com/datatracker/requestdetail/2443
 */
interface UserProfileReviewTracker {

    /** Row 36 */
    fun clickReviewTab(userId: String, isSelf: Boolean)

    /** Row 37 */
    fun clickUserProfileSettings(userId: String)

    /** Row 38 */
    fun clickReviewSettingsToggle(userId: String, isOn: Boolean)

    /** Row 39 */
    fun clickReviewMedia(userId: String, feedbackId: String, isSelf: Boolean, productId: String)

    /** Row 40 */
    fun clickLikeReview(userId: String, feedbackId: String, isSelf: Boolean, productId: String)

    /** Row 41 */
    fun impressReviewCard(userId: String, feedbackId: String, isSelf: Boolean, productId: String, position: Int)

    /** Row 42 */
    fun openScreenEmptyOrHiddenReviewTab()

    /** Row 46 */
    fun clickReviewSeeMoreDescription(userId: String, feedbackId: String, isSelf: Boolean, productId: String)

    /** Row 48 */
    fun clickReviewProductInfo(userId: String, feedbackId: String, isSelf: Boolean, productReview: UserReviewUiModel.Product)
}
