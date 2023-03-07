package com.tokopedia.play.util.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View

/**
 * Created by jegul on 15/04/20
 */
object PlayAnimationUtil {

    private const val FULL_VISIBILITY_ALPHA = 1.0f
    private const val FULL_INVISIBILITY_ALPHA = 0.0f

    fun fadeInAnimation(
            view: View,
            durationInMs: Long,
            fromAlpha: Float = FULL_INVISIBILITY_ALPHA
    ) = fadeAnimation(view, durationInMs, fromAlpha, FULL_VISIBILITY_ALPHA)

    fun fadeOutAnimation(
            view: View,
            durationInMs: Long,
            fromAlpha: Float = FULL_VISIBILITY_ALPHA
    ) = fadeAnimation(view, durationInMs, fromAlpha, FULL_INVISIBILITY_ALPHA)

    fun fadeInThenFadeOutAnimatino(
            view: View,
            durationInMs: Long,
            delayInMs: Long,
            fromAlpha: Float = FULL_INVISIBILITY_ALPHA,
            fadeInListener: Animator.AnimatorListener = createAnimatorListener(),
            delayListener: Animator.AnimatorListener = createAnimatorListener(),
            fadeOutListener: Animator.AnimatorListener = createAnimatorListener()
    ): Animator {
        val animatorSet = AnimatorSet()
        val fadeIn = fadeInAnimation(view, durationInMs, fromAlpha).apply {
            addListener(fadeInListener)
        }
        val delay = delay(delayInMs).apply {
            addListener(delayListener)
        }
        val fadeOut = fadeOutAnimation(view, durationInMs)

        animatorSet.playSequentially(fadeIn, delay, fadeOut)
        return animatorSet
    }

    private fun fadeAnimation(
            view: View,
            durationInMs: Long,
            fromAlpha: Float,
            toAlpha: Float
    ): Animator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, fromAlpha, toAlpha).apply {
            duration = durationInMs
        }
    }

    fun delay(durationInMs: Long): Animator {
        return ValueAnimator.ofInt(0).apply {
            duration = durationInMs
        }
    }

    fun createAnimatorListener(
            onStart: (Animator) -> Unit = {},
            onCancel: (Animator) -> Unit = {},
            onEnd: (Animator) -> Unit = {}
    ) = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) { onEnd(animation) }
        override fun onAnimationCancel(animation: Animator) { onCancel(animation) }
        override fun onAnimationStart(animation: Animator) { onStart(animation) }
    }
}
