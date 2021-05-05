package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewProgressBarState
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography


class CreateReviewProgressBar : BaseCustomView {

    companion object {
        private const val COMPLETE_PROGRESS = 100
        private const val PARTIALLY_COMPLETE_PROGRESS = 66
        private const val EMPTY_PROGRESS = 33
    }

    private var progressBar: ProgressBarUnify? = null
    private var progressBarText: Typography? = null

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
        View.inflate(context, R.layout.widget_create_review_progress_bar, this)
        progressBar = findViewById(R.id.review_form_progress_bar)
        progressBarText = findViewById(R.id.review_form_progress_bar_description)
        progressBar?.progressBarHeight = ProgressBarUnify.SIZE_SMALL
    }

    fun setProgressBarValue(progress: CreateReviewProgressBarState) {
        when {
            progress.isComplete() -> {
                setCompleteProgress()
                if(progress.isGoodRating) {
                    setGoodRatingCompleteText()
                    return
                }
                setBadRatingCompleteText()
            }
            progress.isNeedPhoto() -> {
                setPartiallyCompleteProgress()
                if(progress.isGoodRating) {
                    setGoodRatingNeedPhotoText()
                    return
                }
                setBadRatingNeedPhotoText()
            }
            progress.isNeedReview() -> {
                setPartiallyCompleteProgress()
                if(progress.isGoodRating) {
                    setGoodRatingNeedReviewText()
                    return
                }
                setBadRatingNeedReviewText()
            }
            else -> {
                setEmptyProgress()
                if(progress.isGoodRating) {
                    setGoodRatingEmptyText()
                    return
                }
                setBadRatingEmptyText()
            }
        }
    }

    private fun setEmptyProgress() {
        progressBar?.setValue(EMPTY_PROGRESS)
    }

    private fun setPartiallyCompleteProgress() {
        progressBar?.setValue(PARTIALLY_COMPLETE_PROGRESS)
    }

    private fun setCompleteProgress() {
        progressBar?.setValue(COMPLETE_PROGRESS)
    }

    private fun setGoodRatingCompleteText() {
        progressBarText?.text = context.getString(R.string.review_form_progress_bar_good_complete)
    }

    private fun setGoodRatingNeedPhotoText() {
        progressBarText?.text = context.getString(R.string.review_form_progress_bar_good_need_photo)
    }

    private fun setGoodRatingNeedReviewText() {
        progressBarText?.text = context.getString(R.string.review_form_progress_bar_good_need_text)
    }

    private fun setGoodRatingEmptyText() {
        progressBarText?.text = context.getString(R.string.review_form_progress_bar_good_empty)
    }

    private fun setBadRatingCompleteText() {
        progressBarText?.text = context.getString(R.string.review_form_progress_bar_good_complete)
    }

    private fun setBadRatingNeedPhotoText() {
        progressBarText?.text = context.getString(R.string.review_form_progress_bar_good_need_photo)
    }

    private fun setBadRatingNeedReviewText() {
        progressBarText?.text = context.getString(R.string.review_form_progress_bar_good_need_text)
    }

    private fun setBadRatingEmptyText() {
        progressBarText?.text = context.getString(R.string.review_form_progress_bar_good_complete)
    }
}