package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewProgressBarState
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography


class CreateReviewProgressBar : BaseCustomView {

    companion object {
        private const val COMPLETE_PROGRESS = 100
        private const val HALF_PROGRESS = 50
        private const val THREE_QUARTERS_PROGRESS = 75
        private const val QUARTER_PROGRESS = 25
        private const val PARTIALLY_COMPLETE_PROGRESS = 66
        private const val EMPTY_PROGRESS = 33
    }

    private var progressBar: ProgressBarUnify? = null
    private var progressBarText: Typography? = null

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
        View.inflate(context,  com.tokopedia.review.R.layout.widget_create_review_progress_bar, this)
        progressBar = findViewById(R.id.review_form_progress_bar)
        progressBarText = findViewById(R.id.review_form_progress_bar_description)
        progressBar?.progressBarHeight = ProgressBarUnify.SIZE_SMALL
    }

    fun setProgressBarValue(progress: CreateReviewProgressBarState) {
        when {
            progress.isComplete() -> {
                setCompleteProgress()
                if (progress.isGoodRating) {
                    setGoodRatingCompleteText()
                    return
                }
                setBadRatingCompleteText()
            }
            progress.isNeedPhotoOnly() -> {
                if (progress.isGoodRating) {
                    setGoodRatingNeedPhotoText()
                    setPartiallyCompleteProgress()
                    return
                }
                setBadRatingFlowNeedPhotoText()
                setThreeQuartersProgress()
                return
            }
            progress.isNeedReviewOnly() -> {
                if (progress.isGoodRating) {
                    setGoodRatingNeedReviewText()
                    setPartiallyCompleteProgress()
                    return
                }
                setNeedReviewOnlyText()
                setThreeQuartersProgress()
                return
            }
            progress.isNeedBadRatingReasonOnly() -> {
                if (progress.isTextAreaFilled && progress.isPhotosFilled) {
                    setThreeQuartersProgress()
                } else {
                    setHalfProgress()
                }
                setNeedBadRatingReasonText()
                return
            }
            progress.isBadRatingReasonSelected && !progress.isGoodRating -> {
                setHalfProgress()
                setNeedReviewOnlyText()
            }
            else -> {
                if (progress.isGoodRating) {
                    setEmptyProgress()
                    setGoodRatingEmptyText()
                    return
                }
                setQuarterProgress()
                setBadRatingEmptyText()
            }
        }
    }

    private fun setEmptyProgress() {
        progressBar?.apply {
            setValue(EMPTY_PROGRESS)
            progressBarColor = intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_Y400), ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_Y400))
        }
    }

    private fun setPartiallyCompleteProgress() {
        progressBar?.apply {
            setValue(PARTIALLY_COMPLETE_PROGRESS)
            progressBarColor = intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_Y300), ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_Y300))
        }
    }

    private fun setCompleteProgress() {
        progressBar?.apply {
            setValue(COMPLETE_PROGRESS)
            progressBarColor = intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_G400), ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_G400))
        }
    }

    private fun setQuarterProgress() {
        progressBar?.apply {
            setValue(QUARTER_PROGRESS)
            progressBarColor = intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_Y400), ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_Y400))
        }
    }

    private fun setHalfProgress() {
        progressBar?.apply {
            setValue(HALF_PROGRESS)
            progressBarColor = intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_Y300), ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_Y300))
        }
    }

    private fun setThreeQuartersProgress() {
        progressBar?.apply {
            setValue(THREE_QUARTERS_PROGRESS)
            progressBarColor = intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_Y300), ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_Y300))
        }
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
        progressBarText?.text = context.getString(R.string.review_form_progress_bar_bad_complete)
    }

    private fun setBadRatingFlowNeedPhotoText() {
        progressBarText?.text = context.getString(R.string.review_form_progress_bar_bad_need_photo)
    }

    private fun setBadRatingEmptyText() {
        progressBarText?.text = context.getString(R.string.review_form_progress_bar_bad_need_bad_rating_reason)
    }

    private fun setNeedReviewOnlyText() {
        progressBarText?.text = context.getString(R.string.review_form_progress_bar_new_flow_need_review_only)
    }

    private fun setNeedBadRatingReasonText() {
        progressBarText?.text = context.getString(R.string.review_form_progress_bar_bad_need_reason)
    }
}