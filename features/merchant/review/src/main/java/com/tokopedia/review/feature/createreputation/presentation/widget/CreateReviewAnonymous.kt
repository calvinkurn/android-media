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
) : BaseReviewCustomView<WidgetCreateReviewAnonymousBinding>(context, attrs, defStyleAttr) {

    private val transitionHandler = TransitionHandler()
    private val checkboxListener = CheckboxListener()
    private val trackingHandler = TrackingHandler()
    private var trackerData: CreateReviewAnonymousUiState.Showing.TrackerData? = null
    private var listener: Listener? = null

    override val binding = WidgetCreateReviewAnonymousBinding.inflate(LayoutInflater.from(context), this, true)

    private val bindingAnonymous = binding.layoutAnonymous
    private val bindingLoading = binding.layoutAnonymousLoading

    init {
        bindingAnonymous.cbCreateReviewAnonymous.setOnCheckedChangeListener(checkboxListener)
    }

    fun updateUi(uiState: CreateReviewAnonymousUiState) {
        when(uiState) {
            is CreateReviewAnonymousUiState.Loading -> {
                showLoading()
                animateShow()
            }
            is CreateReviewAnonymousUiState.Showing -> {
                trackerData = uiState.trackerData
                showAnonymous(uiState.checked)
                animateShow()
            }
        }
    }

    fun setListener(newCreateReviewAnonymousListener: Listener) {
        listener = newCreateReviewAnonymousListener
    }

    private fun showLoading() = transitionHandler.transitionToShowLoading()

    private fun showAnonymous(checked: Boolean) {
        transitionHandler.transitionToShowAnonymous()
        setupCheckbox(checked)
        setupSeeAnonymousInfoButton()
    }

    private fun setupCheckbox(checked: Boolean) {
        bindingAnonymous.cbCreateReviewAnonymous.isChecked = checked
    }

    private fun setupSeeAnonymousInfoButton() {
        bindingAnonymous.icCreateReviewAnonymousInfo.setOnClickListener {
            listener?.onClickSeeAnonymousInfo()
        }
    }

    private inner class TransitionHandler {
        private val fadeTransition by lazy(LazyThreadSafetyMode.NONE) {
            Fade().apply {
                duration = ANIMATION_DURATION
                addTarget(bindingAnonymous.root)
                addTarget(bindingLoading.root)
                interpolator = AccelerateInterpolator()
            }
        }

        private fun showLoadingLayout() {
            bindingLoading.root.show()
        }

        private fun hideLoadingLayout() {
            bindingLoading.root.gone()
        }

        private fun showAnonymousLayout() {
            bindingAnonymous.root.show()
        }

        private fun hideAnonymousLayout() {
            bindingAnonymous.root.gone()
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
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            listener?.onIsAnonymousChanged(isChecked)
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
        fun onClickSeeAnonymousInfo()
    }
}
