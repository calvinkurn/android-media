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
import com.tokopedia.review.common.presentation.widget.ReviewBadRatingReasonWidget
import com.tokopedia.review.common.presentation.widget.ReviewBasicInfoWidget
import com.tokopedia.review.feature.credibility.presentation.activity.ReviewCredibilityActivity
import com.tokopedia.review.feature.imagepreview.analytics.ReviewImagePreviewTracking
import com.tokopedia.review.feature.imagepreview.presentation.uimodel.ReviewImagePreviewBottomSheetUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

class ReviewImagePreviewExpandedReviewBottomSheet : BottomSheetUnify(), ReviewBasicInfoListener {

    companion object {
        const val REVIEW_GALLERY_EXPANDED_REVIEW_BOTTOM_SHEET_TAG =
            "Review Gallery Expanded Review BottomSheet Tag"

        fun createInstance(
            reviewImagePreviewBottomSheetUiModel: ReviewImagePreviewBottomSheetUiModel
        ): ReviewImagePreviewExpandedReviewBottomSheet {
            return ReviewImagePreviewExpandedReviewBottomSheet().apply {
                this.uiModel = reviewImagePreviewBottomSheetUiModel
            }
        }
    }

    private var uiModel: ReviewImagePreviewBottomSheetUiModel =
        ReviewImagePreviewBottomSheetUiModel()

    private var basicInfoWidget: ReviewBasicInfoWidget? = null
    private var review: Typography? = null
    private var badRatingWidget: ReviewBadRatingReasonWidget? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            View.inflate(context, com.tokopedia.review.R.layout.bottomsheet_image_preview_expanded_review_detail, null)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        hideHeader()
        setBasicInfo()
        setReview()
        setBadRatingReason()
    }

    override fun onUserNameClicked(userId: String) {
        dismiss()
        goToReviewCredibility(userId, uiModel.source)
    }

    override fun trackOnUserInfoClicked(feedbackId: String, userId: String, statistics: String) {
        ReviewImagePreviewTracking.trackClickReviewerName(
            uiModel.isFromGallery,
            feedbackId,
            userId,
            statistics,
            uiModel.productId,
            uiModel.currentUserId
        )
    }

    private fun setBasicInfo() {
        basicInfoWidget?.apply {
            with(uiModel) {
                setCredibilityData(isProductReview, isAnonymous, userId, feedbackId)
                setListener(this@ReviewImagePreviewExpandedReviewBottomSheet)
                setRating(rating)
                setCreateTime(timeStamp)
                setReviewerName(reviewerName)
                setVariantName(variantName)
                setStats(userStats)
                setReviewerImage(reviewerImage)
            }
        }
    }

    private fun setReview() {
        review?.apply {
            text = HtmlLinkHelper(context, uiModel.reviewMessage).spannedString
            movementMethod = ScrollingMovementMethod()
            maxLines = Int.MAX_VALUE
        }
    }

    private fun setBadRatingReason() {
        badRatingWidget?.showBadRatingReason(uiModel.badRatingReason)
    }

    private fun bindViews(view: View) {
        basicInfoWidget = view.findViewById(R.id.review_gallery_expanded_review_detail_basic_info)
        review = view.findViewById(R.id.review_gallery_expanded_review)
        badRatingWidget = view.findViewById(R.id.review_gallery_expanded_review_bad_reason)
    }

    private fun hideHeader() {
        bottomSheetHeader.hide()
    }

    private fun goToReviewCredibility(userId: String, source: String) {
        RouteManager.route(
            context,
            Uri.parse(
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.REVIEW_CREDIBILITY,
                    userId,
                    source
                )
            ).buildUpon()
                .appendQueryParameter(ReviewCredibilityActivity.PARAM_PRODUCT_ID, uiModel.productId)
                .build()
                .toString()
        )
    }
}