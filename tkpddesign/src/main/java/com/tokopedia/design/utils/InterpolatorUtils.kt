package com.tokopedia.design.utils

import android.animation.TimeInterpolator
import android.support.annotation.IntRange
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.animation.*

object InterpolatorUtils {
    val ACCELERATE_DECELERATE_INTERPOLATOR = 0
    val ACCELERATE_INTERPOLATOR = 1
    val ANTICIPATE_INTERPOLATOR = 2
    val ANTICIPATE_OVERSHOOT_INTERPOLATOR = 3
    val BOUNCE_INTERPOLATOR = 4
    val DECELERATE_INTERPOLATOR = 5
    val FAST_OUT_LINEAR_IN_INTERPOLATOR = 6
    val FAST_OUT_SLOW_IN_INTERPOLATOR = 7
    val LINEAR_INTERPOLATOR = 8
    val LINEAR_OUT_SLOW_IN_INTERPOLATOR = 9
    val OVERSHOOT_INTERPOLATOR = 10

    /**
     * Creates interpolator.
     *
     * @param interpolatorType
     * @return
     */
    fun createInterpolator(@IntRange(from = 0, to = 10) interpolatorType: Int): TimeInterpolator {
        return when (interpolatorType) {
            ACCELERATE_DECELERATE_INTERPOLATOR -> AccelerateDecelerateInterpolator()
            ACCELERATE_INTERPOLATOR -> AccelerateInterpolator()
            ANTICIPATE_INTERPOLATOR -> AnticipateInterpolator()
            ANTICIPATE_OVERSHOOT_INTERPOLATOR -> AnticipateOvershootInterpolator()
            BOUNCE_INTERPOLATOR -> BounceInterpolator()
            DECELERATE_INTERPOLATOR -> DecelerateInterpolator()
            FAST_OUT_LINEAR_IN_INTERPOLATOR -> FastOutLinearInInterpolator()
            FAST_OUT_SLOW_IN_INTERPOLATOR -> FastOutSlowInInterpolator()
            LINEAR_INTERPOLATOR -> LinearInterpolator()
            LINEAR_OUT_SLOW_IN_INTERPOLATOR -> LinearOutSlowInInterpolator()
            OVERSHOOT_INTERPOLATOR -> OvershootInterpolator()
            else -> LinearInterpolator()
        }
    }
}