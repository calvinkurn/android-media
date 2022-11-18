package com.tokopedia.review.feature.credibility.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.databinding.WidgetReviewCredibilityHeaderBinding
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseReviewCustomView
import com.tokopedia.review.feature.credibility.analytics.ReviewCredibilityTracking
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityHeaderUiModel
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityHeaderUiState

class ReviewCredibilityHeaderWidget @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseReviewCustomView<WidgetReviewCredibilityHeaderBinding>(
    context, attributeSet, defStyleAttr
) {
    override val binding: WidgetReviewCredibilityHeaderBinding =
        WidgetReviewCredibilityHeaderBinding.inflate(
            LayoutInflater.from(context), this, true
        )

    private val headerBinding by lazy(LazyThreadSafetyMode.NONE) {
        binding.layoutReviewCredibilityHeader
    }

    private val headerLoadingBinding by lazy(LazyThreadSafetyMode.NONE) {
        binding.layoutReviewCredibilityHeaderLoading
    }

    private var listener: Listener? = null

    fun updateUiState(uiState: ReviewCredibilityHeaderUiState) {
        when (uiState) {
            is ReviewCredibilityHeaderUiState.Hidden -> hideWidget()
            is ReviewCredibilityHeaderUiState.Loading -> showLoading()
            is ReviewCredibilityHeaderUiState.Showed -> showData(uiState.data)
        }
    }

    fun setListener(newListener: Listener) {
        listener = newListener
    }

    private fun hideWidget() {
        animateHide(onAnimationEnd = {
            listener?.onHeaderTransitionEnd()
        })
    }

    private fun showLoading() {
        headerBinding.root.gone()
        headerLoadingBinding.root.show()
        animateShow(onAnimationEnd = {
            listener?.onHeaderTransitionEnd()
        })
    }

    private fun showData(data: ReviewCredibilityHeaderUiModel) {
        runTransitions(createShowDataTransition())
        showUIData(data)
        animateShow(onAnimationEnd = {
            listener?.onHeaderTransitionEnd()
        })
    }

    private fun showUIData(data: ReviewCredibilityHeaderUiModel) {
        setupReviewerProfilePicture(data.reviewerProfilePicture)
        setupReviewerName(data.reviewerName)
        setupReviewerJoinDate(data.reviewerJoinDate)
        setupReviewerProfileButton(data.reviewerProfileButtonText, data.reviewerProfileButtonUrl, data.trackingData)
        headerLoadingBinding.root.gone()
        headerBinding.root.show()
    }

    private fun setupReviewerProfileButton(
        reviewerProfileButtonText: String,
        reviewerProfileUrl: String,
        trackingData: ReviewCredibilityHeaderUiModel.TrackingData
    ) {
        headerBinding.btnReviewCredibilityHeaderReviewerSeeProfile.apply {
            text = reviewerProfileButtonText
            setOnClickListener {
                if (RouteManager.route(context, reviewerProfileUrl)) {
                    ReviewCredibilityTracking.trackClickSeeProfileButton(trackingData)
                }
            }
            showWithCondition(shouldShow = reviewerProfileButtonText.isNotBlank() && reviewerProfileUrl.isNotBlank())
        }
    }

    private fun setupReviewerProfilePicture(reviewerProfilePicture: String) {
        headerBinding.ivReviewCredibilityHeaderReviewerProfilePicture.loadImage(reviewerProfilePicture)
    }

    private fun setupReviewerName(reviewerName: String) {
        headerBinding.tvReviewCredibilityHeaderReviewerName.text = reviewerName
    }

    private fun setupReviewerJoinDate(reviewerFollowersCount: String) {
        headerBinding.tvReviewCredibilityHeaderReviewerJoinDate.apply {
            text = reviewerFollowersCount
            showWithCondition(reviewerFollowersCount.isNotBlank())
        }
    }

    private fun createShowDataTransition(): Transition {
        return Fade().setInterpolator(
            PathInterpolatorCompat.create(
                CUBIC_BEZIER_X1, CUBIC_BEZIER_Y1, CUBIC_BEZIER_X2, CUBIC_BEZIER_Y2
            )
        ).setDuration(ANIMATION_DURATION)
    }

    private fun runTransitions(transition: Transition) {
        TransitionManager.endTransitions(binding.root)
        TransitionManager.beginDelayedTransition(binding.root, transition)
    }

    interface Listener {
        fun onHeaderTransitionEnd()
    }
}
