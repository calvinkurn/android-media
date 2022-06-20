package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateInterpolator
import android.widget.CompoundButton
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetCreateReviewAnonymousBinding
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewAnonymousUiState

class CreateReviewAnonymous @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCreateReviewCustomView<WidgetCreateReviewAnonymousBinding>(context, attrs, defStyleAttr) {

    companion object {
        private const val TRANSITION_DURATION = 300L
    }

    private val transitionHandler = TransitionHandler()
    private val checkboxListener = CheckboxListener()
    private val trackingHandler = TrackingHandler()
    private var trackerData: CreateReviewAnonymousUiState.Showing.TrackerData? = null

    override val binding = WidgetCreateReviewAnonymousBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.layoutAnonymous.root.setOnCheckedChangeListener(checkboxListener)
    }

    private fun showLoading() = transitionHandler.transitionToShowLoading()

    private fun WidgetCreateReviewAnonymousBinding.showAnonymous(checked: Boolean) {
        transitionHandler.transitionToShowAnonymous()
        setupCheckbox(checked)
    }

    private fun WidgetCreateReviewAnonymousBinding.setupCheckbox(checked: Boolean) {
        layoutAnonymous.root.isChecked = checked
    }

    fun updateUi(uiState: CreateReviewAnonymousUiState) {
        when(uiState) {
            is CreateReviewAnonymousUiState.Loading -> {
                showLoading()
                animateShow()
            }
            is CreateReviewAnonymousUiState.Showing -> {
                trackerData = uiState.trackerData
                binding.showAnonymous(uiState.checked)
                animateShow()
            }
        }
    }

    fun setListener(newCreateReviewAnonymousListener: Listener) {
        checkboxListener.createReviewAnonymousListener = newCreateReviewAnonymousListener
    }

    private inner class TransitionHandler {
        private val fadeTransition by lazy(LazyThreadSafetyMode.NONE) {
            Fade().apply {
                duration = TRANSITION_DURATION
                addTarget(binding.layoutAnonymous.root)
                addTarget(binding.layoutAnonymousLoading.root)
                interpolator = AccelerateInterpolator()
            }
        }

        private fun WidgetCreateReviewAnonymousBinding.showLoadingLayout() {
            layoutAnonymousLoading.root.show()
        }

        private fun WidgetCreateReviewAnonymousBinding.hideLoadingLayout() {
            layoutAnonymousLoading.root.gone()
        }

        private fun WidgetCreateReviewAnonymousBinding.showAnonymousLayout() {
            layoutAnonymous.root.show()
        }

        private fun WidgetCreateReviewAnonymousBinding.hideAnonymousLayout() {
            layoutAnonymous.root.gone()
        }

        private fun WidgetCreateReviewAnonymousBinding.beginDelayedTransition() {
            TransitionManager.beginDelayedTransition(root, fadeTransition)
        }

        fun transitionToShowAnonymous() {
            with(binding) {
                beginDelayedTransition()
                hideLoadingLayout()
                showAnonymousLayout()
            }
        }

        fun transitionToShowLoading() {
            with(binding) {
                beginDelayedTransition()
                hideAnonymousLayout()
                showLoadingLayout()
            }
        }
    }

    private inner class CheckboxListener: CompoundButton.OnCheckedChangeListener {
        var createReviewAnonymousListener: Listener? = null
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            createReviewAnonymousListener?.onIsAnonymousChanged(isChecked)
            trackingHandler.trackReviewOnAnonymousChecked(isChecked)
        }
    }

    private inner class TrackingHandler {
        fun trackReviewOnAnonymousChecked(isChecked: Boolean) {
            if (isChecked) {
                trackerData?.let { trackerData ->
                    CreateReviewTracking.reviewOnAnonymousClickTracker(
                        orderId = trackerData.orderId,
                        productId = trackerData.productId,
                        isEditReview = trackerData.editMode,
                        feedbackId = trackerData.feedbackId
                    )
                }
            }
        }
    }

    interface Listener {
        fun onIsAnonymousChanged(anonymous: Boolean)
    }
}