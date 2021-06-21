package com.tokopedia.review.feature.reading.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class ReadReviewRatingOnlyEmptyState : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var circularProgressBar: ProgressBar? = null
    private var satisfactionRate: Typography? = null
    private var ratingCount: Typography? = null
    private var fiveStarProgressBar: ReadReviewRatingBar? = null
    private var fourStarProgressBar: ReadReviewRatingBar? = null
    private var threeStarProgressBar: ReadReviewRatingBar? = null
    private var twoStarProgressBar: ReadReviewRatingBar? = null
    private var oneStarProgressBar: ReadReviewRatingBar? = null

    private fun init() {
        View.inflate(context, R.layout.widget_read_review_rating_only_empty_state, this)
        bindViews()
    }

    private fun bindViews() {
        circularProgressBar = findViewById(R.id.rating_only_circular_progress_bar)
        satisfactionRate = findViewById(R.id.rating_only_satisfaction_rate)
        ratingCount = findViewById(R.id.rating_only_count)
        fiveStarProgressBar = findViewById(R.id.rating_only_statistic_five_progress_bar)
        fourStarProgressBar = findViewById(R.id.rating_only_statistic_four_progress_bar)
        threeStarProgressBar = findViewById(R.id.rating_only_statistic_three_progress_bar)
        twoStarProgressBar = findViewById(R.id.rating_only_statistic_two_progress_bar)
        oneStarProgressBar = findViewById(R.id.rating_only_statistic_one_progress_bar)
    }
}