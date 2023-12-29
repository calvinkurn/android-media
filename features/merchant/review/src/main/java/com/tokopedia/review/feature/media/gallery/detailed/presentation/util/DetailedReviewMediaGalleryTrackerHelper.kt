package com.tokopedia.review.feature.media.gallery.detailed.presentation.util

/**
 * Created By : Jonathan Darwin on June 23, 2023
 */
object DetailedReviewMediaGalleryTrackerHelper {

    const val EVENT_ACTION_CLICK_SEE_ALL_FROM_USER_PROFILE = "click - review selengkapnya"
    const val EVENT_ACTION_IMPRESS_SEE_ALL_BOTTOM_SHEET_FROM_USER_PROFILE = "impression - review detail"
    const val EVENT_ACTION_CLICK_LIKE_FROM_USER_PROFILE = "click - like review"
    const val EVENT_ACTION_IMPRESS_REVIEW_MEDIA_FROM_USER_PROFILE = "impression - review media"

    const val EVENT_CATEGORY_FEED_USER_PROFILE = "feed user profile - review media detail"
    const val BUSINESS_UNIT = "content"

    private const val SELF = "self"
    private const val VISITOR = "visitor"

    fun getEventLabel(
        feedbackId: String,
        productId: String,
        reviewUserId: String,
        isReviewOwner: Boolean
    ): String {
        return "$feedbackId - $reviewUserId - ${getSelfOrVisitor(isReviewOwner)} - $productId"
    }

    private fun getSelfOrVisitor(isReviewOwner: Boolean): String {
        return if (isReviewOwner) SELF
        else VISITOR
    }
}
