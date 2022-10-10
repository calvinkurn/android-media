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
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.review.databinding.WidgetCreateReviewRatingBinding
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewRatingUiState
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class CreateReviewRating @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetCreateReviewRatingBinding>(context, attrs, defStyleAttr) {

    private val ratingListener = RatingListener()
    private val transitionHandler = TransitionHandler()
    private val trackingHandler = TrackingHandler()

    override val binding = WidgetCreateReviewRatingBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.layoutRating.reviewFormRating.setListener(ratingListener)
        binding.layoutRating.reviewFormRating.resetStars()
    }

    private fun showLoading() {
        transitionHandler.transitionToShowLoading()
    }

    private fun WidgetCreateReviewRatingBinding.showData(
        uiState: CreateReviewRatingUiState.Showing
    ) {
        transitionHandler.transitionToShowData()
        setupRating(uiState.rating)
    }

    private fun WidgetCreateReviewRatingBinding.setupRating(rating: Int) {
        layoutRating.reviewFormRating.setRating(rating)
    }

    fun updateUi(uiState: CreateReviewRatingUiState, continuation: Continuation<Unit>) {
        when (uiState) {
            is CreateReviewRatingUiState.Loading -> {
                showLoading()
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
            is CreateReviewRatingUiState.Showing -> {
                trackingHandler.trackerData = uiState.trackerData
                binding.showData(uiState)
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
        }
    }

    fun setListener(newCreateReviewRatingListener: Listener) {
        ratingListener.listener = newCreateReviewRatingListener
    }

    private inner class RatingListener :
        AnimatedRatingPickerCreateReviewView.AnimatedReputationListener {
        var listener: Listener? = null

        override fun onClick(position: Int) {
            listener?.onRatingChanged(position)
            trackingHandler.trackRatingChanged(position)
        }
    }

    private inner class TrackingHandler {
        var trackerData: CreateReviewRatingUiState.Showing.TrackerData? = null

        fun trackRatingChanged(position: Int) {
            trackerData?.let {
                CreateReviewTracking.reviewOnRatingChangedTracker(
                    orderId = it.orderId,
                    productId = it.productId,
                    ratingValue = position.toString(),
                    isSuccessful = true,
                    isEditReview = it.editMode,
                    feedbackId = it.feedbackId
                )
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

    interface Listener {
        fun onRatingChanged(rating: Int)
    }
}