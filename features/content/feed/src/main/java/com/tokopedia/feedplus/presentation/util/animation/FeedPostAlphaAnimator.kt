package com.tokopedia.feedplus.presentation.util.animation

import android.animation.Animator
import android.animation.ValueAnimator
import androidx.annotation.FloatRange

/**
 * Created by kenny.hadisaputra on 08/08/23
 */
class FeedPostAlphaAnimator(
    private val listener: Listener
) {

    private var mAlphaAnimator: Animator? = null
    private var mOpacityAnimator: Animator? = null

    fun animateToOpaque(@FloatRange(from = 0.0, to = 1.0) startAlpha: Float) {
        if (mOpacityAnimator?.isRunning == true) return
        mAlphaAnimator?.cancel()

        val animator = ValueAnimator.ofFloat(startAlpha, 1.0f)
            .setDuration(ANIMATION_DURATION)

        animator.addUpdateListener { valAnimator ->
            listener.onAnimateAlpha(this, valAnimator.animatedValue as Float)
        }

        mOpacityAnimator = animator
        animator.start()
    }

    fun animateToAlpha(@FloatRange(from = 0.0, to = 1.0) startAlpha: Float) {
        if (mAlphaAnimator?.isRunning == true) return
        mOpacityAnimator?.cancel()

        val animator = ValueAnimator.ofFloat(startAlpha, 0.3f)
            .setDuration(ANIMATION_DURATION)

        animator.addUpdateListener { valAnimator ->
            listener.onAnimateAlpha(this, valAnimator.animatedValue as Float)
        }

        mAlphaAnimator = animator
        animator.start()
    }

    companion object {
        private const val ANIMATION_DURATION = 100L
    }

    interface Listener {
        fun onAnimateAlpha(animator: FeedPostAlphaAnimator, alpha: Float)
    }
}
