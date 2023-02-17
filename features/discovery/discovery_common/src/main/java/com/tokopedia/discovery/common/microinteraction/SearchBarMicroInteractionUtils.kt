package com.tokopedia.discovery.common.microinteraction

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import androidx.core.view.animation.PathInterpolatorCompat

private const val CUBIC_BEZIER_START_X = 0.63f
private const val CUBIC_BEZIER_START_Y = 0.01f
private const val CUBIC_BEZIER_END_X = 0.29f
private const val CUBIC_BEZIER_END_Y = 1.0f
private const val DEFAULT_ANIMATED_VALUE = 0f

internal fun ValueAnimator.animatedValue() = (animatedValue as? Float) ?: DEFAULT_ANIMATED_VALUE

internal fun onAnimationEndListener(onAnimationEnd: (Animator) -> Unit) =
    object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) { }

        override fun onAnimationEnd(animation: Animator) {
            onAnimationEnd(animation)
        }

        override fun onAnimationCancel(animation: Animator) { }

        override fun onAnimationRepeat(animation: Animator) { }
    }

internal fun searchBarMicroInteractionAnimator(
    start: Float,
    end: Float,
    duration: Long,
    onUpdateListener: (Float) -> Unit,
): ValueAnimator =
    ValueAnimator.ofFloat(start, end).apply {
        this.interpolator = cubicBezierInterpolator()
        this.duration = duration
        this.addUpdateListener { onUpdateListener(it.animatedValue()) }
    }

private fun cubicBezierInterpolator(): TimeInterpolator =
    PathInterpolatorCompat.create(
        CUBIC_BEZIER_START_X,
        CUBIC_BEZIER_START_Y,
        CUBIC_BEZIER_END_X,
        CUBIC_BEZIER_END_Y,
    )
