package com.tokopedia.scp_rewards_touchpoints.common.util

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import com.tokopedia.unifycomponents.ImageUnify

object ViewUtil {
    private const val START_ROTATION_DEFAULT_VALUE = 0F
    private const val END_ROTATION_DEFAULT_VALUE = 360F
    private const val ROTATION_ANIMATION_DEFAULT_DURATION = 5000L

    fun ImageUnify.rotate(
        startValue: Float = START_ROTATION_DEFAULT_VALUE,
        endValue: Float = END_ROTATION_DEFAULT_VALUE,
        animationDuration: Long = ROTATION_ANIMATION_DEFAULT_DURATION
    ) {
        ObjectAnimator.ofFloat(
            this,
            View.ROTATION,
            startValue,
            endValue
        ).apply {
            duration = animationDuration
            interpolator = null
            repeatCount = ValueAnimator.INFINITE
            start()
        }
    }
}
