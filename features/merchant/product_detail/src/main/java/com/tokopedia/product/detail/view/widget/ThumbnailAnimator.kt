package com.tokopedia.product.detail.view.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import androidx.core.view.animation.PathInterpolatorCompat
import com.tokopedia.kotlin.extensions.view.show

class ThumbnailAnimator(val view: View) {

    companion object {
        private const val CUBIC_BEZIER_X1 = 0.63f
        private const val CUBIC_BEZIER_X2 = 0.29f
        private const val CUBIC_BEZIER_Y1 = 0.01f
        private const val CUBIC_BEZIER_Y2 = 1f
    }

    private fun calculateWrapHeight(): Int {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return view.measuredHeight
    }

    private fun createHeightAnimator(end: Int): Animator {
        return ValueAnimator.ofInt(end).apply {
            interpolator = PathInterpolatorCompat.create(CUBIC_BEZIER_X1, CUBIC_BEZIER_Y1, CUBIC_BEZIER_X2, CUBIC_BEZIER_Y2)
            addUpdateListener { newValue ->
                val layoutParamsCopy = view.layoutParams
                layoutParamsCopy.height = (newValue.animatedValue as Int)
                view.layoutParams = layoutParamsCopy
            }
        }
    }

    fun animateShow() {
        val measuredWrapHeight = calculateWrapHeight()
        val anim = createHeightAnimator(end = measuredWrapHeight)
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                view.show()
            }

            override fun onAnimationEnd(animation: Animator?) {}

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationRepeat(animation: Animator?) {}
        })
        anim.duration = 300L
        anim.start()
    }
}