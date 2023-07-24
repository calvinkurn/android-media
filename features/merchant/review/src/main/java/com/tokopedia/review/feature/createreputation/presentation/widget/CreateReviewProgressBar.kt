package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.databinding.WidgetCreateReviewProgressBarBinding
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewProgressBarState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewProgressBarUiState

class CreateReviewProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetCreateReviewProgressBarBinding>(context, attrs, defStyleAttr) {

    companion object {
        private const val COMPLETE_PROGRESS = 100
        private const val HALF_PROGRESS = 50
        private const val THREE_QUARTERS_PROGRESS = 75
        private const val QUARTER_PROGRESS = 25
        private const val PARTIALLY_COMPLETE_PROGRESS = 66
        private const val EMPTY_PROGRESS = 33
    }

    private val transitionHandler = TransitionHandler()

    override val binding = WidgetCreateReviewProgressBarBinding.inflate(LayoutInflater.from(context), this, true)

    private fun showLoading() = transitionHandler.transitionToShowLoading()

    private fun WidgetCreateReviewProgressBarBinding.showProgressBar(
        uiState: CreateReviewProgressBarUiState.Showing
    ) {
        transitionHandler.transitionToShowProgressBar()
        setupProgressBarState(uiState.state)
    }

    private fun WidgetCreateReviewProgressBarBinding.setupProgressBarState(
        state: CreateReviewProgressBarState
    ) {
        when {
            state.isComplete() -> {
                setCompleteProgress()
                if (state.isGoodRating) {
                    setGoodRatingCompleteText()
                    return
                }
                setBadRatingCompleteText()
            }
            state.isNeedPhotoOnly() -> {
                if (state.isGoodRating) {
                    setGoodRatingNeedPhotoText()
                    setPartiallyCompleteProgress()
                    return
                }
                setBadRatingFlowNeedPhotoText()
                setThreeQuartersProgress()
                return
            }
            state.isNeedReviewOnly() -> {
                if (state.isGoodRating) {
                    setGoodRatingNeedReviewText()
                    setPartiallyCompleteProgress()
                    return
                }
                setNeedReviewOnlyText()
                setThreeQuartersProgress()
                return
            }
            state.isNeedBadRatingReasonOnly() -> {
                if (state.isTestimonyComplete && state.isPhotosFilled) {
                    setThreeQuartersProgress()
                } else {
                    setHalfProgress()
                }
                setNeedBadRatingReasonText()
                return
            }
            state.isBadRatingReasonSelected && !state.isGoodRating -> {
                setHalfProgress()
                setNeedReviewOnlyText()
            }
            else -> {
                if (state.isGoodRating) {
                    setEmptyProgress()
                    setGoodRatingEmptyText()
                    return
                }
                setQuarterProgress()
                setBadRatingEmptyText()
            }
        }
    }

    private fun WidgetCreateReviewProgressBarBinding.setEmptyProgress() {
        layoutProgressBar.reviewFormProgressBar.apply {
            setValue(EMPTY_PROGRESS)
            progressBarColor = intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN400), ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN400))
        }
    }

    private fun WidgetCreateReviewProgressBarBinding.setPartiallyCompleteProgress() {
        layoutProgressBar.reviewFormProgressBar.apply {
            setValue(PARTIALLY_COMPLETE_PROGRESS)
            progressBarColor = intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN300), ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN300))
        }
    }

    private fun WidgetCreateReviewProgressBarBinding.setCompleteProgress() {
        layoutProgressBar.reviewFormProgressBar.apply {
            setValue(COMPLETE_PROGRESS)
            progressBarColor = intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500), ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
        }
    }

    private fun WidgetCreateReviewProgressBarBinding.setQuarterProgress() {
        layoutProgressBar.reviewFormProgressBar.apply {
            setValue(QUARTER_PROGRESS)
            progressBarColor = intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN400), ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN400))
        }
    }

    private fun WidgetCreateReviewProgressBarBinding.setHalfProgress() {
        layoutProgressBar.reviewFormProgressBar.apply {
            setValue(HALF_PROGRESS)
            progressBarColor = intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN300), ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN300))
        }
    }

    private fun WidgetCreateReviewProgressBarBinding.setThreeQuartersProgress() {
        layoutProgressBar.reviewFormProgressBar.apply {
            setValue(THREE_QUARTERS_PROGRESS)
            progressBarColor = intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN300), ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN300))
        }
    }

    private fun WidgetCreateReviewProgressBarBinding.setGoodRatingCompleteText() {
        layoutProgressBar.reviewFormProgressBarDescription.text = context.getString(R.string.review_form_progress_bar_good_complete)
    }

    private fun WidgetCreateReviewProgressBarBinding.setGoodRatingNeedPhotoText() {
        layoutProgressBar.reviewFormProgressBarDescription.text = context.getString(R.string.review_form_progress_bar_good_need_photo)
    }

    private fun WidgetCreateReviewProgressBarBinding.setGoodRatingNeedReviewText() {
        layoutProgressBar.reviewFormProgressBarDescription.text = context.getString(R.string.review_form_progress_bar_good_need_text)
    }

    private fun WidgetCreateReviewProgressBarBinding.setGoodRatingEmptyText() {
        layoutProgressBar.reviewFormProgressBarDescription.text = context.getString(R.string.review_form_progress_bar_good_empty)
    }

    private fun WidgetCreateReviewProgressBarBinding.setBadRatingCompleteText() {
        layoutProgressBar.reviewFormProgressBarDescription.text = context.getString(R.string.review_form_progress_bar_bad_complete)
    }

    private fun WidgetCreateReviewProgressBarBinding.setBadRatingFlowNeedPhotoText() {
        layoutProgressBar.reviewFormProgressBarDescription.text = context.getString(R.string.review_form_progress_bar_bad_need_photo)
    }

    private fun WidgetCreateReviewProgressBarBinding.setBadRatingEmptyText() {
        layoutProgressBar.reviewFormProgressBarDescription.text = context.getString(R.string.review_form_progress_bar_bad_need_bad_rating_reason)
    }

    private fun WidgetCreateReviewProgressBarBinding.setNeedReviewOnlyText() {
        layoutProgressBar.reviewFormProgressBarDescription.text = context.getString(R.string.review_form_progress_bar_new_flow_need_review_only)
    }

    private fun WidgetCreateReviewProgressBarBinding.setNeedBadRatingReasonText() {
        layoutProgressBar.reviewFormProgressBarDescription.text = context.getString(R.string.review_form_progress_bar_bad_need_reason)
    }

    fun updateUi(uiState: CreateReviewProgressBarUiState) {
        when(uiState) {
            is CreateReviewProgressBarUiState.Loading -> {
                showLoading()
                animateShow()
            }
            is CreateReviewProgressBarUiState.Showing -> {
                binding.showProgressBar(uiState)
                animateShow()
            }
        }
    }

    private inner class TransitionHandler {
        private val fadeTransition by lazy(LazyThreadSafetyMode.NONE) {
            Fade().apply {
                duration = ANIMATION_DURATION
                addTarget(binding.layoutProgressBar.root)
                addTarget(binding.layoutProgressBarLoading.root)
                interpolator = AccelerateInterpolator()
            }
        }

        private fun WidgetCreateReviewProgressBarBinding.showLoadingLayout() {
            layoutProgressBarLoading.root.show()
        }

        private fun WidgetCreateReviewProgressBarBinding.hideLoadingLayout() {
            layoutProgressBarLoading.root.gone()
        }

        private fun WidgetCreateReviewProgressBarBinding.showProgressBarLayout() {
            layoutProgressBar.root.show()
        }

        private fun WidgetCreateReviewProgressBarBinding.hideProgressBarLayout() {
            layoutProgressBar.root.gone()
        }

        private fun WidgetCreateReviewProgressBarBinding.beginDelayedTransition() {
            TransitionManager.beginDelayedTransition(root, fadeTransition)
        }

        fun transitionToShowProgressBar() {
            with(binding) {
                beginDelayedTransition()
                hideLoadingLayout()
                showProgressBarLayout()
            }
        }

        fun transitionToShowLoading() {
            with(binding) {
                beginDelayedTransition()
                hideProgressBarLayout()
                showLoadingLayout()
            }
        }
    }
}
