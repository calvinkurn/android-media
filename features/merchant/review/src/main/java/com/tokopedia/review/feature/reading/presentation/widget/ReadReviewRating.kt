package com.tokopedia.review.feature.reading.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class ReadReviewRating : BaseCustomView {

    private var rating: Typography? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_read_review_rating, this)
        bindViews()
    }

    private fun bindViews() {
        rating = findViewById(R.id.read_review_overall_rating)
    }

    fun setRating(ratingScore: String) {
        rating?.text = ratingScore
    }
}