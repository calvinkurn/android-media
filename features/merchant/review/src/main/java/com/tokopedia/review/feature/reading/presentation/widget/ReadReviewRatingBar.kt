package com.tokopedia.review.feature.reading.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography

class ReadReviewRatingBar : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        setStar(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
        setStar(attrs, defStyleAttr)
    }

    private var rating: Int = 0
    private var ratingTypography: Typography? = null
    private var ratingCountTypography: Typography? = null
    private var progressBar: ProgressBarUnify? = null

    private fun init() {
        View.inflate(context, R.layout.widget_read_review_rating_detail, this)
        ratingTypography = findViewById(R.id.review_reading_rating)
        ratingCountTypography = findViewById(R.id.review_reading_rating_count)
        progressBar = findViewById(R.id.review_reading_progress_bar)
    }

    private fun setStar(attrs: AttributeSet, defStyleAttr: Int = 0) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ReadReviewRatingBar, defStyleAttr, 0)
        rating = typedArray.getInt(R.styleable.ReadReviewRatingBar_rating, 0)
        ratingTypography?.text = rating.toString()
    }

    fun setProgressAndRatingCount(progress: Int, ratingCount: Long) {
        progressBar?.setValue(progress)
        ratingCountTypography?.text = ratingCount.toString()
    }
}