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
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class CreateReviewSubmitButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetCreateReviewSubmitButtonBinding>(context, attrs, defStyleAttr) {

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

    fun updateUi(uiState: CreateReviewSubmitButtonUiState, continuation: Continuation<Unit>) {
        when(uiState) {
            is CreateReviewSubmitButtonUiState.Loading -> {
                showLoading()
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
            is CreateReviewSubmitButtonUiState.Enabled -> {
                binding.showButton(enabled = true, sending = false)
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
            is CreateReviewSubmitButtonUiState.Disabled -> {
                binding.showButton(enabled = false, sending = false)
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
            is CreateReviewSubmitButtonUiState.Sending -> {
                binding.showButton(enabled = false, sending = true)
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
        }
    }

    fun setListener(newCreateReviewSubmitButtonListener: Listener) {
        createReviewSubmitButtonListener = newCreateReviewSubmitButtonListener
    }

    private inner class TransitionHandler {
        private val fadeTransition by lazy(LazyThreadSafetyMode.NONE) {
            Fade().apply {
                duration = ANIMATION_DURATION
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