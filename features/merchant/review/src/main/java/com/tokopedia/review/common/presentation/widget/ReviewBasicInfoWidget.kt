package com.tokopedia.review.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ReviewBasicInfoWidget : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var ratingStars: ImageUnify? = null
    private var timeStamp: Typography? = null
    private var reviewerName: Typography? = null

    private fun init() {
        View.inflate(context, R.layout.widget_review_basic_info, this)
        bindViews()
    }

    private fun bindViews() {
        ratingStars = findViewById(R.id.review_item_rating)
        timeStamp = findViewById(R.id.review_item_timestamp)
        reviewerName = findViewById(R.id.review_item_reviewer_name)
    }

    fun setRating(rating: Int) {
        ratingStars?.loadImage(getReviewStar(rating))
    }

    fun setCreateTime(createTime: String) {
        timeStamp?.text = createTime
    }

    fun setReviewerName(name: String) {
        reviewerName?.text = name
    }
}