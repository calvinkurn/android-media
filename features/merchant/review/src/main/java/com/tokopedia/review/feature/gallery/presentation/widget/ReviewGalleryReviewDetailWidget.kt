package com.tokopedia.review.feature.gallery.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.widget.ReviewBasicInfoWidget
import com.tokopedia.review.common.util.ReviewUtil
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class ReviewGalleryReviewDetailWidget : BaseCustomView {

    companion object {
        private const val MAX_LINES = 3
        private const val MAX_CHAR = 140
        private const val ALLOW_CLICK = true
    }

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
    private var likeButton: IconUnify? = null

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
        likeButton = findViewById(R.id.review_gallery_like_icon)
    }

    fun setPhotoCount(index: Int, total: Int) {
        photoCount?.text = context.getString(R.string.review_gallery_image_count, index, total)
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

    fun setLikeButtonClickListener(action: () -> Unit) {
        likeButton?.setOnClickListener {
            action.invoke()
        }
    }

    fun setLikeButtonImage(isLiked: Boolean) {
        if (isLiked) {
            likeButton?.setImage(IconUnify.THUMB_FILLED)
        } else {
            likeButton?.setImage(IconUnify.THUMB)
        }
    }

    fun setReviewMessage(reviewMessage: String, action: () -> Unit) {
        if (reviewMessage.isEmpty()) {
            reviewText?.text = context.getString(R.string.review_reading_empty_review)
            return
        }
        reviewText?.apply {
            isEnabled = true
            val formattingResult = ReviewUtil.reviewDescFormatter(context, reviewMessage, MAX_CHAR, ALLOW_CLICK)
            maxLines = MAX_LINES
            text = formattingResult.first
            if (formattingResult.second) {
                setOnClickListener {
                    action.invoke()
                }
            }
            show()
        }
    }

}