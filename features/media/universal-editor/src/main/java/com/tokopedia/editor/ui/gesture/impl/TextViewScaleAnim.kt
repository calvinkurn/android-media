package com.tokopedia.editor.ui.gesture.impl

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

object TextViewScaleAnim {

    fun anim(view: View, scaleX: Float, scaleY: Float) {
        // Create ObjectAnimator for scaleX and scaleY
        val scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", scaleX)
        val scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", scaleY)

        // Set animation duration and interpolator
        val duration = 200L
        scaleXAnimator.duration = duration
        scaleYAnimator.duration = duration

        // Use an interpolator for smooth scaling
        val interpolator = AccelerateDecelerateInterpolator()
        scaleXAnimator.interpolator = interpolator
        scaleYAnimator.interpolator = interpolator

        // Start animation
        scaleXAnimator.start()
        scaleYAnimator.start()
    }
}
