package com.tokopedia.review.feature.gallery.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.widget.ReviewBasicInfoWidget
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class ReviewGalleryExpandedReviewBottomSheet : BottomSheetUnify() {

    companion object {
        const val REVIEW_GALLERY_EXPANDED_REVIEW_BOTTOM_SHEET_TAG = "Review Gallery Expanded Review BottomSheet Tag"
        fun createInstance(rating: Int, timeStamp: String, reviewerName: String, reviewMessage: String): ReviewGalleryExpandedReviewBottomSheet {
            return ReviewGalleryExpandedReviewBottomSheet().apply {
                this.rating = rating
                this.timeStamp = timeStamp
                this.reviewerName = reviewerName
                this.reviewMessage = reviewMessage
            }
        }
    }

    private var rating: Int = 0
    private var timeStamp = ""
    private var reviewerName = ""
    private var reviewMessage = ""

    private var basicInfoWidget: ReviewBasicInfoWidget? = null
    private var review: Typography? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, R.layout.bottomsheet_review_gallery_expanded_review_detail, null)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setBasicInfo()
        setReview()
//        setStateChangeListener()
        setDefaultState()
    }

    private fun setBasicInfo() {
        basicInfoWidget?.apply {
            setRating(rating)
            setCreateTime(timeStamp)
            setReviewerName(reviewerName)
        }
    }

    private fun setReview() {
        review?.text = reviewMessage
    }

    private fun bindViews(view: View) {
        basicInfoWidget = view.findViewById(R.id.review_gallery_expanded_review_detail_basic_info)
        review = view.findViewById(R.id.review_gallery_expanded_review)
    }

    private fun setDefaultState() {
       bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setStateChangeListener() {
        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // No Op
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if(newState == BottomSheetBehavior.STATE_EXPANDED) {
                    isFullpage = true
                }
            }
        })
    }
}