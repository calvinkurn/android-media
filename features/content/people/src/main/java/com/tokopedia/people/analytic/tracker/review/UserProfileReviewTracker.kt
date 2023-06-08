package com.tokopedia.people.analytic.tracker.review

/**
 * Created By : Jonathan Darwin on June 07, 2023
 */

/**
 * Mynakama Tracker
 * https://mynakama.tokopedia.com/datatracker/requestdetail/2443
 */
interface UserProfileReviewTracker {

    /** Row 36 */
    fun clickReviewTab(isSelf: Boolean)

    /** Row 37 */
    fun clickUserProfileSettings()

    /** Row 38 */
    fun clickReviewSettingsToggle(isOn: Boolean)

    /** Row 39 */
    fun clickReviewMedia(feedbackId: String, isSelf: Boolean, productId: String)

    /** Row 40 */
    fun clickLikeReview(feedbackId: String, isSelf: Boolean, productId: String)

    /** Row 41 */ /** TODO: send as list or? */
    fun impressReviewCard(position: Int, feedbackId: String, isSelf: Boolean, productId: String)

    /** Row 42 */
    fun openScreenEmptyOrHiddenReviewTab()

    /** Row 46 */
    fun clickReviewSeeMoreDescription(feedbackId: String, isSelf: Boolean, productId: String)

    /** Row 48 */
    fun clickReviewProductInfo(feedbackId: String, isSelf: Boolean, productId: String)
}
