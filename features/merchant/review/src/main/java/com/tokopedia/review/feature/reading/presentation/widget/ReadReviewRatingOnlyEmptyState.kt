package com.tokopedia.review.feature.reading.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.review.R
import com.tokopedia.review.feature.reading.data.ProductRating
import com.tokopedia.review.feature.reading.data.ProductReviewDetail
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class ReadReviewRatingOnlyEmptyState : BaseCustomView {

    companion object {
        private const val MAX_RATING = 5
        private const val PERCENT_MULTIPLIER = 100
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

    private var rating: ReadReviewRating? = null
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
        rating = findViewById(R.id.rating_only_rating_score)
        circularProgressBar = findViewById(R.id.rating_only_circular_progress_bar)
        satisfactionRate = findViewById(R.id.rating_only_satisfaction_rate)
        ratingCount = findViewById(R.id.rating_only_count)
        fiveStarProgressBar = findViewById(R.id.rating_only_statistic_five_progress_bar)
        fourStarProgressBar = findViewById(R.id.rating_only_statistic_four_progress_bar)
        threeStarProgressBar = findViewById(R.id.rating_only_statistic_three_progress_bar)
        twoStarProgressBar = findViewById(R.id.rating_only_statistic_two_progress_bar)
        oneStarProgressBar = findViewById(R.id.rating_only_statistic_one_progress_bar)
    }

    private fun bindProgressBarData(progressBar: ReadReviewRatingBar?, reviewDetail: ProductReviewDetail) {
        progressBar?.setProgressAndRatingCount(reviewDetail.percentage, reviewDetail.totalReviews)
    }

    private fun getListOfProgressBars(): List<ReadReviewRatingBar?> {
        return listOf(fiveStarProgressBar, fourStarProgressBar, threeStarProgressBar, twoStarProgressBar, oneStarProgressBar)
    }

    private fun setRatingProgress(ratingDetail: List<ProductReviewDetail>) {
        val progressBars = getListOfProgressBars()
        ratingDetail.forEachIndexed { index, productReviewDetail ->
            if (productReviewDetail.rate == ratingDetail.size - index) {
                bindProgressBarData(progressBars[index], productReviewDetail)
            }
        }
    }

    private fun setCircularProgressBarProgress(ratingScore: Float) {
        val rating = ratingScore.div(MAX_RATING).times(PERCENT_MULTIPLIER).toInt()
        circularProgressBar?.progress = rating
    }

    fun setRatingData(productRating: ProductRating) {
        rating?.setRating(productRating.ratingScore)
        setCircularProgressBarProgress(productRating.ratingScore.toFloatOrZero())
        this.satisfactionRate?.text = productRating.satisfactionRate
        this.ratingCount?.text = context.getString(R.string.review_reading_rating_only_count, productRating.totalRating)
        setRatingProgress(productRating.detail)
    }
}