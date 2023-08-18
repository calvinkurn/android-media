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
import com.tokopedia.review.databinding.WidgetCreateReviewTextAreaTitleBinding
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTextAreaTitleUiState
import com.tokopedia.reviewcommon.uimodel.StringRes

class CreateReviewTextAreaTitle @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetCreateReviewTextAreaTitleBinding>(context, attrs, defStyleAttr) {

    private val transitionHandler = TransitionHandler()

    override val binding = WidgetCreateReviewTextAreaTitleBinding.inflate(LayoutInflater.from(context), this, true)

    private fun showLoading() = transitionHandler.transitionToShowLoading()

    private fun WidgetCreateReviewTextAreaTitleBinding.showTitle(uiState: CreateReviewTextAreaTitleUiState.Showing) {
        transitionHandler.transitionToShowTextAreaTitle()
        setupTitle(uiState.textRes)
    }

    private fun WidgetCreateReviewTextAreaTitleBinding.setupTitle(textRes: StringRes) {
        layoutTextAreaTitle.root.text = textRes.getStringValue(context)
    }

    fun updateUi(uiState: CreateReviewTextAreaTitleUiState) {
        when(uiState) {
            is CreateReviewTextAreaTitleUiState.Loading -> {
                showLoading()
                animateShow()
            }
            is CreateReviewTextAreaTitleUiState.Showing -> {
                binding.showTitle(uiState)
                animateShow()
            }
        }
    }

    fun setListener(newBaseCreateReviewCustomViewListener: Listener) {
        baseCreateReviewCustomViewListener = newBaseCreateReviewCustomViewListener
    }

    private inner class TransitionHandler {
        private val fadeTransition by lazy(LazyThreadSafetyMode.NONE) {
            Fade().apply {
                duration = ANIMATION_DURATION
                addTarget(binding.layoutTextAreaTitle.root)
                addTarget(binding.layoutTextAreaTitleLoading.root)
                interpolator = AccelerateInterpolator()
            }
        }

        private fun WidgetCreateReviewTextAreaTitleBinding.showTitleLayout() {
            layoutTextAreaTitle.root.show()
        }

        private fun WidgetCreateReviewTextAreaTitleBinding.hideTitleLayout() {
            layoutTextAreaTitle.root.gone()
        }

        private fun WidgetCreateReviewTextAreaTitleBinding.showLoadingLayout() {
            layoutTextAreaTitleLoading.root.show()
        }

        private fun WidgetCreateReviewTextAreaTitleBinding.hideLoadingLayout() {
            layoutTextAreaTitleLoading.root.gone()
        }

        private fun WidgetCreateReviewTextAreaTitleBinding.beginDelayedTransition() {
            TransitionManager.beginDelayedTransition(root, fadeTransition)
        }

        fun transitionToShowTextAreaTitle() {
            with(binding) {
                beginDelayedTransition()
                hideLoadingLayout()
                showTitleLayout()
            }
        }

        fun transitionToShowLoading() {
            with(binding) {
                beginDelayedTransition()
                hideTitleLayout()
                showLoadingLayout()
            }
        }
    }
}
