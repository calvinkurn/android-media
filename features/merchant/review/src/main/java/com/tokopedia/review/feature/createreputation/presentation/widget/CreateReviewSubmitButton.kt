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
import com.tokopedia.review.databinding.WidgetCreateReviewSubmitButtonBinding
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewSubmitButtonUiState

class CreateReviewSubmitButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCreateReviewCustomView<WidgetCreateReviewSubmitButtonBinding>(context, attrs, defStyleAttr) {

    companion object {
        private const val TRANSITION_DURATION = 300L
    }

    private val transitionHandler = TransitionHandler()
    private var createReviewSubmitButtonListener: Listener? = null

    override val binding = WidgetCreateReviewSubmitButtonBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.layoutSubmitButton.root.setOnClickListener { createReviewSubmitButtonListener?.onSubmitButtonClicked() }
    }

    private fun showLoading() = transitionHandler.transitionToShowLoading()

    private fun WidgetCreateReviewSubmitButtonBinding.showButton(
        enabled: Boolean,
        sending: Boolean
    ) {
        transitionHandler.transitionToShowSubmitButton()
        setupButton(enabled, sending)
    }

    private fun WidgetCreateReviewSubmitButtonBinding.setupButton(
        enabled: Boolean,
        sending: Boolean
    ) {
        layoutSubmitButton.root.isEnabled = enabled
        layoutSubmitButton.root.isLoading = sending
    }

    fun updateUi(uiState: CreateReviewSubmitButtonUiState) {
        when(uiState) {
            is CreateReviewSubmitButtonUiState.Loading -> {
                showLoading()
                animateShow()
            }
            is CreateReviewSubmitButtonUiState.Enabled -> {
                binding.showButton(enabled = true, sending = false)
                animateShow()
            }
            is CreateReviewSubmitButtonUiState.Disabled -> {
                binding.showButton(enabled = false, sending = false)
                animateShow()
            }
            is CreateReviewSubmitButtonUiState.Sending -> {
                binding.showButton(enabled = false, sending = true)
                animateShow()
            }
        }
    }

    fun setListener(newCreateReviewSubmitButtonListener: Listener) {
        createReviewSubmitButtonListener = newCreateReviewSubmitButtonListener
    }

    private inner class TransitionHandler {
        private val fadeTransition by lazy(LazyThreadSafetyMode.NONE) {
            Fade().apply {
                duration = TRANSITION_DURATION
                addTarget(binding.layoutSubmitButton.root)
                addTarget(binding.layoutSubmitButtonLoading.root)
                interpolator = AccelerateInterpolator()
            }
        }

        private fun WidgetCreateReviewSubmitButtonBinding.showLoadingLayout() {
            layoutSubmitButtonLoading.root.show()
        }

        private fun WidgetCreateReviewSubmitButtonBinding.hideLoadingLayout() {
            layoutSubmitButtonLoading.root.gone()
        }

        private fun WidgetCreateReviewSubmitButtonBinding.showSubmitButtonLayout() {
            layoutSubmitButton.root.show()
        }

        private fun WidgetCreateReviewSubmitButtonBinding.hideSubmitButtonLayout() {
            layoutSubmitButton.root.gone()
        }

        private fun WidgetCreateReviewSubmitButtonBinding.beginDelayedTransition() {
            TransitionManager.beginDelayedTransition(root, fadeTransition)
        }

        fun transitionToShowSubmitButton() {
            with(binding) {
                beginDelayedTransition()
                hideLoadingLayout()
                showSubmitButtonLayout()
            }
        }

        fun transitionToShowLoading() {
            with(binding) {
                beginDelayedTransition()
                hideSubmitButtonLayout()
                showLoadingLayout()
            }
        }
    }

    interface Listener {
        fun onSubmitButtonClicked()
    }
}