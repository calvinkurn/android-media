package com.tokopedia.editor.ui.gesture.util

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

fun View.animationScale(scaleX: Float, scaleY: Float) {
    // Create ObjectAnimator for scaleX and scaleY
    val scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", scaleX)
    val scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", scaleY)

    // Set animation duration and interpolator
    val duration = 200L
    scaleXAnimator.duration = duration
    scaleYAnimator.duration = duration

    // Use an interpolator for smooth scaling
    val interpolator = AccelerateDecelerateInterpolator()
    scaleXAnimator.interpolator = interpolator
    scaleYAnimator.interpolator = interpolator

    // Ensure the animation only once executed
    scaleXAnimator.repeatCount = 0
    scaleYAnimator.repeatCount = 0

    // Start animation
    scaleXAnimator.start()
    scaleYAnimator.start()
}
