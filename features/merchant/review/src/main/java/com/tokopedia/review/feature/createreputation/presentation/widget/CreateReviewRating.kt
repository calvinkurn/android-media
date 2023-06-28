package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateInterpolator
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetCreateReviewRatingBinding
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewRatingUiState
import com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating.WidgetReviewAnimatedRating
import com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating.WidgetReviewAnimatedRatingConfig

class CreateReviewRating @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetCreateReviewRatingBinding>(context, attrs, defStyleAttr) {

    private val transitionHandler = TransitionHandler()

    override val binding = WidgetCreateReviewRatingBinding.inflate(LayoutInflater.from(context), this, true)

    private fun showLoading() {
        transitionHandler.transitionToShowLoading()
    }

    private fun WidgetCreateReviewRatingBinding.showData(
        uiState: CreateReviewRatingUiState.Showing
    ) {
        transitionHandler.transitionToShowData()
        setupRating(uiState.widgetConfig)
    }

    private fun WidgetCreateReviewRatingBinding.setupRating(
        widgetConfig: WidgetReviewAnimatedRatingConfig
    ) {
        layoutRating.reviewFormRating.setContent {
            WidgetReviewAnimatedRating(config = widgetConfig)
        }
    }

    fun updateUi(uiState: CreateReviewRatingUiState) {
        when (uiState) {
            is CreateReviewRatingUiState.Loading -> {
                showLoading()
                animateShow()
            }
            is CreateReviewRatingUiState.Showing -> {
                binding.showData(uiState)
                animateShow()
            }
        }
    }

    private inner class TransitionHandler {
        private val fadeTransition by lazy(LazyThreadSafetyMode.NONE) {
            Fade().apply {
                duration = ANIMATION_DURATION
                addTarget(binding.layoutRating.root)
                addTarget(binding.layoutRatingLoading.root)
                interpolator = AccelerateInterpolator()
            }
        }

        private fun WidgetCreateReviewRatingBinding.showDataLayout() {
            layoutRating.root.show()
        }

        private fun WidgetCreateReviewRatingBinding.hideDataLayout() {
            layoutRating.root.gone()
        }

        private fun WidgetCreateReviewRatingBinding.showLoadingLayout() {
            layoutRatingLoading.root.show()
        }

        private fun WidgetCreateReviewRatingBinding.hideLoadingLayout() {
            layoutRatingLoading.root.gone()
        }

        private fun WidgetCreateReviewRatingBinding.beginDelayedTransition() {
            TransitionManager.beginDelayedTransition(root, fadeTransition)
        }

        fun transitionToShowData() {
            with(binding) {
                beginDelayedTransition()
                hideLoadingLayout()
                showDataLayout()
            }
        }

        fun transitionToShowLoading() {
            with(binding) {
                beginDelayedTransition()
                hideDataLayout()
                showLoadingLayout()
            }
        }
    }
}
