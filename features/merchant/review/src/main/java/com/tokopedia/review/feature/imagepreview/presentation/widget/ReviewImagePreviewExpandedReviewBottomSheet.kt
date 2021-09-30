package com.tokopedia.review.feature.imagepreview.presentation.widget

import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.listener.ReviewBasicInfoListener
import com.tokopedia.review.common.presentation.widget.ReviewBasicInfoWidget
import com.tokopedia.review.feature.credibility.presentation.activity.ReviewCredibilityActivity
import com.tokopedia.review.feature.imagepreview.analytics.ReviewImagePreviewTracking
import com.tokopedia.review.feature.reading.data.UserReviewStats
import com.tokopedia.review.feature.reading.presentation.fragment.ReadReviewFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

class ReviewImagePreviewExpandedReviewBottomSheet : BottomSheetUnify(), ReviewBasicInfoListener {

    companion object {
        const val REVIEW_GALLERY_EXPANDED_REVIEW_BOTTOM_SHEET_TAG =
            "Review Gallery Expanded Review BottomSheet Tag"

        fun createInstance(
            rating: Int, timeStamp: String, reviewerName: String,
            reviewMessage: String, variantName: String = "",
            userStats: List<UserReviewStats> = listOf(), userId: String = "",
            isAnonymous: Boolean = false, isProductReview: Boolean = false, feedbackId: String = "",
            productId: String = "", isFromGallery: Boolean, currentUserId: String,
            reviewerImage: String
        ): ReviewImagePreviewExpandedReviewBottomSheet {
            return ReviewImagePreviewExpandedReviewBottomSheet().apply {
                this.rating = rating
                this.timeStamp = timeStamp
                this.reviewerName = reviewerName
                this.reviewMessage = reviewMessage
                this.variantName = variantName
                this.userStats = userStats
                this.userId = userId
                this.isAnonymous = isAnonymous
                this.isProductReview = isProductReview
                this.feedbackId = feedbackId
                this.productId = productId
                this.isFromGallery = isFromGallery
                this.currentUserId = currentUserId
                this.reviewerImage = reviewerImage
            }
        }
    }

    private var rating: Int = 0
    private var timeStamp = ""
    private var reviewerName = ""
    private var reviewMessage = ""
    private var variantName = ""
    private var userStats = listOf<UserReviewStats>()
    private var userId = ""
    private var isAnonymous = false
    private var isProductReview = false
    private var feedbackId = ""
    private var productId = ""
    private var isFromGallery = false
    private var currentUserId = ""
    private var reviewerImage = ""

    private var basicInfoWidget: ReviewBasicInfoWidget? = null
    private var review: Typography? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            View.inflate(context, R.layout.bottomsheet_image_preview_expanded_review_detail, null)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        hideHeader()
        setBasicInfo()
        setReview()
    }

    override fun onUserNameClicked(userId: String) {
        dismiss()
        goToReviewCredibility(userId)
    }

    override fun trackOnUserInfoClicked(feedbackId: String, userId: String, statistics: String) {
        ReviewImagePreviewTracking.trackClickReviewerName(isFromGallery, feedbackId, userId, statistics, productId, currentUserId)
    }

    private fun setBasicInfo() {
        basicInfoWidget?.apply {
            setCredibilityData(isProductReview, isAnonymous, userId, feedbackId)
            setRating(rating)
            setCreateTime(timeStamp)
            setReviewerName(reviewerName)
            setVariantName(variantName)
            setStats(userStats)
            setListener(this@ReviewImagePreviewExpandedReviewBottomSheet)
            setReviewerImage(reviewerImage)
            setCountColorToGreen()
        }
    }

    private fun setReview() {
        review?.apply {
            text = HtmlLinkHelper(context, reviewMessage).spannedString
            movementMethod = ScrollingMovementMethod()
            maxLines = Int.MAX_VALUE
        }
    }

    private fun bindViews(view: View) {
        basicInfoWidget = view.findViewById(R.id.review_gallery_expanded_review_detail_basic_info)
        review = view.findViewById(R.id.review_gallery_expanded_review)
    }

    private fun hideHeader() {
        bottomSheetHeader.hide()
    }

    private fun goToReviewCredibility(userId: String) {
        RouteManager.route(
            context,
            Uri.parse(
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.REVIEW_CREDIBILITY,
                    userId,
                    ReadReviewFragment.READING_SOURCE
                )
            ).buildUpon()
                .appendQueryParameter(ReviewCredibilityActivity.PARAM_PRODUCT_ID, productId).build()
                .toString()
        )
    }
}