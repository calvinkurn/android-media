package com.tokopedia.review.feature.gallery.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.widget.ReviewBasicInfoWidget
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class ReviewGalleryReviewDetailWidget : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var photoCount: Typography? = null
    private var basicInfo: ReviewBasicInfoWidget? = null
    private var reviewText: Typography? = null
    private var likeCount: Typography? = null

    private fun init() {
        View.inflate(context, R.layout.widget_review_gallery_review_detail, this)
        bindViews()
        basicInfo?.invertColors()
    }

    private fun bindViews() {
        photoCount = findViewById(R.id.review_gallery_photo_count)
        basicInfo = findViewById(R.id.review_gallery_basic_info)
        reviewText = findViewById(R.id.review_gallery_review)
        likeCount = findViewById(R.id.review_gallery_like_count)
    }

    fun setPhotoCount(index: Int, total: Int) {
        photoCount?.text = "$index/$total"
    }

    fun setRating(rating: Int) {
        basicInfo?.setRating(rating)
    }

    fun setTimeStamp(timeStamp: String) {
        basicInfo?.setCreateTime(timeStamp)
    }

    fun setReviewerName(reviewerName: String) {
        basicInfo?.setReviewerName(reviewerName)
    }

    fun setLikeCount(totalLike: Int) {
        likeCount?.text = totalLike.toString()
    }

    fun setReviewMessage(reviewMessage: String) {
        reviewText?.text = reviewMessage
    }

}