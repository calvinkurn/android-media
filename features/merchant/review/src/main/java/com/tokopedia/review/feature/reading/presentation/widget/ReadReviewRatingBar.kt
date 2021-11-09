package com.tokopedia.review.feature.reading.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography

class ReadReviewRatingBar : BaseCustomView {

    companion object {
        const val ZERO_PERCENT = 0F
        const val ONE_PERCENT = 1F
    }

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
        progressBar?.progressBarColor = intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_Y300), ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_Y300))
    }

    private fun setStar(attrs: AttributeSet, defStyleAttr: Int = 0) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ReadReviewRatingBar, defStyleAttr, 0)
        rating = typedArray.getInt(R.styleable.ReadReviewRatingBar_read_review_rating, 0)
        ratingTypography?.text = rating.toString()
    }


    private fun getAdjustedProgress(percentage: Float): Int {
        return if (percentage < ONE_PERCENT && percentage != ZERO_PERCENT) {
            ONE_PERCENT.toInt()
        } else {
            percentage.toInt()
        }
    }

    fun setProgressAndRatingCount(percentage: Float, formattedRatingCount: String) {
        progressBar?.setValue(getAdjustedProgress(percentage))
        ratingCountTypography?.text = formattedRatingCount
    }
}