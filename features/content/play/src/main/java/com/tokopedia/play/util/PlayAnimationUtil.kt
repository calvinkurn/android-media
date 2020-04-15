package com.tokopedia.play.util

import android.animation.Animator
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

    fun fadeAnimation(
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
}