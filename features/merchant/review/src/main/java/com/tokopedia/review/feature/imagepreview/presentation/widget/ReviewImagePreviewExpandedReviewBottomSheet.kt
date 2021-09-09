package com.tokopedia.review.feature.imagepreview.presentation.widget

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.widget.ReviewBasicInfoWidget
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class ReviewImagePreviewExpandedReviewBottomSheet : BottomSheetUnify() {

    companion object {
        const val REVIEW_GALLERY_EXPANDED_REVIEW_BOTTOM_SHEET_TAG = "Review Gallery Expanded Review BottomSheet Tag"
        fun createInstance(rating: Int, timeStamp: String, reviewerName: String, reviewMessage: String, variantName: String = ""): ReviewImagePreviewExpandedReviewBottomSheet {
            return ReviewImagePreviewExpandedReviewBottomSheet().apply {
                this.rating = rating
                this.timeStamp = timeStamp
                this.reviewerName = reviewerName
                this.reviewMessage = reviewMessage
                this.variantName = variantName
            }
        }
    }

    private var rating: Int = 0
    private var timeStamp = ""
    private var reviewerName = ""
    private var reviewMessage = ""
    private var variantName = ""
    private var userId = ""

    private var basicInfoWidget: ReviewBasicInfoWidget? = null
    private var review: Typography? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, R.layout.bottomsheet_image_preview_expanded_review_detail, null)
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

    private fun setBasicInfo() {
        basicInfoWidget?.apply {
            setRating(rating)
            setCreateTime(timeStamp)
            setReviewerName(reviewerName)
            setVariantName(variantName)
        }
    }

    private fun setReview() {
        review?.apply {
            text = reviewMessage
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
}