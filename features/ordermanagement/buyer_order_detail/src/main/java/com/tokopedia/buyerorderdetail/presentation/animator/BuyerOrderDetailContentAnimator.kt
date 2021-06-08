package com.tokopedia.buyerorderdetail.presentation.animator

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class BuyerOrderDetailContentAnimator(
        private val swipeRefreshLayout: SwipeRefreshLayout?,
        private val recyclerView: RecyclerView?
) {

    companion object {
        private const val SHOW_HIDE_CONTENT_ANIMATION_DURATION = 300L
        private const val FADE_ANIMATION_DELAY = 60L
        private const val TRANSLATION_ANIMATION_DELAY = 45L
    }

    private var animatorShowContent: AnimatorSet? = null
    private var animatorHideContent: AnimatorSet? = null

    private fun createShowContentAnimatorSet(): AnimatorSet? {
        return if (swipeRefreshLayout == null || recyclerView == null) {
            null
        } else {
            val showContentAnimator = createTranslationYAnimator(swipeRefreshLayout, swipeRefreshLayout.translationY, 0f)
            val fadeInAnimator = createFadeAnimator(recyclerView, recyclerView.alpha, 1f)
            AnimatorSet().apply {
                playTogether(showContentAnimator, fadeInAnimator)
            }
        }
    }

    private fun createHideContentAnimatorSet(): AnimatorSet? {
        return if (swipeRefreshLayout == null || recyclerView == null) {
            null
        } else {
            val hideContentAnimator = createTranslationYAnimator(swipeRefreshLayout, swipeRefreshLayout.translationY, swipeRefreshLayout.measuredHeight.toFloat())
            val fadeOutAnimator = createFadeAnimator(recyclerView, recyclerView.alpha, 0f)
            AnimatorSet().apply {
                playTogether(hideContentAnimator, fadeOutAnimator)
            }
        }
    }

    private fun createFadeAnimator(target: View, from: Float, to: Float): Animator {
        return ObjectAnimator.ofFloat(target, View.ALPHA, from, to).apply {
            interpolator = DecelerateInterpolator(2f)
            duration = SHOW_HIDE_CONTENT_ANIMATION_DURATION
            startDelay = FADE_ANIMATION_DELAY
        }
    }

    private fun createTranslationYAnimator(target: View, from: Float, to: Float): Animator {
        return ObjectAnimator.ofFloat(target, View.TRANSLATION_Y, from, to).apply {
            interpolator = DecelerateInterpolator()
            duration = SHOW_HIDE_CONTENT_ANIMATION_DURATION
            startDelay = TRANSLATION_ANIMATION_DELAY
        }
    }

    fun showContent() {
        animatorShowContent = createShowContentAnimatorSet()
        animatorHideContent?.pause()
        animatorShowContent?.start()
    }

    fun hideContent() {
        animatorHideContent = createHideContentAnimatorSet()
        animatorShowContent?.pause()
        animatorHideContent?.start()
    }
}