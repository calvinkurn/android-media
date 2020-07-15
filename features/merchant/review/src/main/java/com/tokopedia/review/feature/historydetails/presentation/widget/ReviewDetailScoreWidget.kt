package com.tokopedia.review.feature.historydetails.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.review.R
import com.tokopedia.review.feature.historydetails.data.ReviewDetailScore
import com.tokopedia.review.feature.historydetails.data.ReviewDetailScoreState
import com.tokopedia.unifycomponents.BaseCustomView

class ReviewDetailScoreWidget : BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_review_detail_score, this)
    }

    fun initScore(reviewDetailScoreState: ReviewDetailScoreState) {
        when(reviewDetailScoreState) {
            is ReviewDetailScoreState.ReviewDetailScoreEditable -> {
                setEditableScore(reviewDetailScoreState.currentScore)
            }
            is ReviewDetailScoreState.ReviewDetailScoreFinal -> {
                setFinalScore(reviewDetailScoreState.currentScore)
            }
        }
    }

    fun updateScore(reviewDetailScoreState: ReviewDetailScoreState.ReviewDetailScoreFinal) {

    }

    private fun setEditableScore(score: ReviewDetailScore) {
        when(score) {
            is ReviewDetailScore.ReviewDetailScoreBad -> {

            }
            is ReviewDetailScore.ReviewDetailScoreMediocre -> {

            }
            is ReviewDetailScore.ReviewDetailScoreExcellent -> {

            }
        }
    }

    private fun setFinalScore(score: ReviewDetailScore) {
        when(score) {
            is ReviewDetailScore.ReviewDetailScoreBad -> {

            }
            is ReviewDetailScore.ReviewDetailScoreMediocre -> {

            }
            is ReviewDetailScore.ReviewDetailScoreExcellent -> {

            }
        }
    }

}