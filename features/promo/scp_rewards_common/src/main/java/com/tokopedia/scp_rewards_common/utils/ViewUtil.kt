package com.tokopedia.scp_rewards_common.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.View

object ViewUtil {
    private const val START_ROTATION_DEFAULT_VALUE = 0F
    private const val END_ROTATION_DEFAULT_VALUE = 360F
    private const val ROTATION_ANIMATION_DEFAULT_DURATION = 5000L

    fun View.rotate(
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

    fun View.scaleAndFadeView(duration: Long, from: Int = 0, to: Int = 1, interpolatorType: Int, listener: Animator.AnimatorListener? = null) {
        val scaleXPvh = getScaleXPropertyValueHolder(from, to)
        val scaleYPvh = getScaleYPropertyValueHolder(from, to)
        val opacityPvh = getOpacityPropertyValueHolder()
        animateView(arrayOf(scaleXPvh, scaleYPvh, opacityPvh), duration = duration, interpolatorType = interpolatorType, listener = listener)
    }

    fun View.translateAndFadeView(duration: Long, from: Int, to: Int = 0, interpolatorType: Int) {
        val translatePvh = getTranslationPropertyValueHolder(from, to)
        val opacityPvh = getOpacityPropertyValueHolder()
        animateView(animations = arrayOf(translatePvh, opacityPvh), duration = duration, interpolatorType = interpolatorType)
    }

    fun View.fadeView(duration: Long, from: Int = 0, to: Int = 255, interpolatorType: Int, listener: Animator.AnimatorListener? = null) {
        val opacityPvh = getOpacityPropertyValueHolder(from, to)
        animateView(animations = arrayOf(opacityPvh), duration = duration, interpolatorType = interpolatorType, listener = listener)
    }

    private fun getTranslationPropertyValueHolder(from: Int, to: Int = 0) = PropertyValuesHolder.ofFloat(
        View.TRANSLATION_Y,
        from.toFloat(),
        to.toFloat()
    )

    private fun getOpacityPropertyValueHolder(from: Int = 0, to: Int = 255) = PropertyValuesHolder.ofFloat(
        View.ALPHA,
        from.toFloat(),
        to.toFloat()
    )

    private fun getScaleXPropertyValueHolder(from: Int = 0, to: Int = 1) = PropertyValuesHolder.ofFloat(
        View.SCALE_X,
        from.toFloat(),
        to.toFloat()
    )

    private fun getScaleYPropertyValueHolder(from: Int = 0, to: Int = 1) = PropertyValuesHolder.ofFloat(
        View.SCALE_Y,
        from.toFloat(),
        to.toFloat()
    )
}
