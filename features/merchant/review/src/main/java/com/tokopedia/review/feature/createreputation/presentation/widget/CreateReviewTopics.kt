package com.tokopedia.review.feature.createreputation.presentation.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.PathInterpolator
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.databinding.WidgetCreateReviewTopicsBinding
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTopicsUiState
import com.tokopedia.unifycomponents.toPx

class CreateReviewTopics @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetCreateReviewTopicsBinding>(context, attrs, defStyleAttr) {

    companion object {
        private const val TOPIC_SEPARATOR = " • "
        private const val AUTO_SCROLL_MAX_DISTANCE = 100

        private const val AUTO_SCROLL_TO_END_ANIMATION_DURATION = 400L
        private const val AUTO_SCROLL_TO_END_ANIMATION_DELAY = 500L
        private const val AUTO_SCROLL_TO_END_CUBIC_BEZIER_X1 = 0.63f
        private const val AUTO_SCROLL_TO_END_CUBIC_BEZIER_X2 = 0.29f
        private const val AUTO_SCROLL_TO_END_CUBIC_BEZIER_Y1 = 0.01f
        private const val AUTO_SCROLL_TO_END_CUBIC_BEZIER_Y2 = 1f

        private const val AUTO_SCROLL_TO_START_ANIMATION_DURATION = 300L
        private const val AUTO_SCROLL_TO_START_ANIMATION_DELAY = 50L
        private const val AUTO_SCROLL_TO_START_CUBIC_BEZIER_X1 = 0.63f
        private const val AUTO_SCROLL_TO_START_CUBIC_BEZIER_X2 = 0.19f
        private const val AUTO_SCROLL_TO_START_CUBIC_BEZIER_Y1 = 0.01f
        private const val AUTO_SCROLL_TO_START_CUBIC_BEZIER_Y2 = 1.55f

        private const val TOPIC_INSPIRATION_END_PADDING = 16
    }

    private var isFirstShow: Boolean = true

    override val binding = WidgetCreateReviewTopicsBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private fun WidgetCreateReviewTopicsBinding.showTopics(topics: List<String>) {
        tvReviewFormTopics.text = topics.joinToString(TOPIC_SEPARATOR)
    }

    fun updateUI(uiState: CreateReviewTopicsUiState) {
        when (uiState) {
            is CreateReviewTopicsUiState.Showing -> {
                binding.showTopics(uiState.topics)
                animateShow(onAnimationEnd = {
                    shouldAnimatePeek(uiState.animatePeek)
                })
            }
            is CreateReviewTopicsUiState.Hidden -> {
                animateHide()
            }
        }
    }

    private fun shouldAnimatePeek(animatePeek: Boolean) {
        if (animatePeek && isFirstShow) {
            isFirstShow = false
            animateScrollToEnd()
        }
    }

    private fun animateScrollToEnd() {
        val topicsEndXPos = binding.tvReviewFormTopics.right + TOPIC_INSPIRATION_END_PADDING.toPx()
        val scrollViewWidth = binding.root.width - TOPIC_INSPIRATION_END_PADDING.toPx()
        val topicsFullScrollDistance = topicsEndXPos - scrollViewWidth
        val scrollerX = topicsFullScrollDistance.coerceAtMost(AUTO_SCROLL_MAX_DISTANCE)
        ValueAnimator.ofInt(Int.ZERO, scrollerX).apply {
            duration = AUTO_SCROLL_TO_END_ANIMATION_DURATION
            startDelay = AUTO_SCROLL_TO_END_ANIMATION_DELAY
            interpolator = PathInterpolator(
                AUTO_SCROLL_TO_END_CUBIC_BEZIER_X1,
                AUTO_SCROLL_TO_END_CUBIC_BEZIER_Y1,
                AUTO_SCROLL_TO_END_CUBIC_BEZIER_X2,
                AUTO_SCROLL_TO_END_CUBIC_BEZIER_Y2
            )
            addUpdateListener { value ->
                binding.root.scrollTo(value.animatedValue as Int, Int.ZERO)
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    animateScrollToStart()
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
            start()
        }
    }

    private fun animateScrollToStart() {
        ValueAnimator.ofInt(binding.root.scrollX, Int.ZERO).apply {
            duration = AUTO_SCROLL_TO_START_ANIMATION_DURATION
            startDelay = AUTO_SCROLL_TO_START_ANIMATION_DELAY
            interpolator = PathInterpolator(
                AUTO_SCROLL_TO_START_CUBIC_BEZIER_X1,
                AUTO_SCROLL_TO_START_CUBIC_BEZIER_Y1,
                AUTO_SCROLL_TO_START_CUBIC_BEZIER_X2,
                AUTO_SCROLL_TO_START_CUBIC_BEZIER_Y2
            )
            addUpdateListener { value ->
                binding.root.scrollTo(value.animatedValue as Int, Int.ZERO)
            }
            start()
        }
    }
}
