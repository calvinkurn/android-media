package com.tokopedia.media.picker.utils.anim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View

object CameraButton {

    private const val CORNER_RADIUS_DURATION = 500L
    private const val BACKGROUND_COLOR = "#EF144A"
    private const val RESIZE_BUTTON = 1.5f

    // we need to ensure that the camera button fully transform to circle
    private const val INIT_VALUE = 100f

    // transform the button to rectangle with corner radius of 20f
    private const val END_VALUE = 20f

    fun View.animStartRecording() {
        animate(true, INIT_VALUE, END_VALUE)
    }

    fun View.animStopRecording() {
        animate(false, END_VALUE, INIT_VALUE)
    }

    private fun View.animate(needToZoomOut: Boolean, init: Float, end: Float) {
        val rectangle = createRectangle()
        background = rectangle

        AnimatorSet().apply {
            duration = CORNER_RADIUS_DURATION

            playTogether(
                rectangle.cornerRadiusAnim(init, end),
                if (needToZoomOut) {
                    zoomOut(rectangle.intrinsicWidth)
                } else {
                    zoomInt(rectangle.intrinsicWidth)
                }
            )
        }.start()
    }

    private fun GradientDrawable.cornerRadiusAnim(init: Float, end: Float): ObjectAnimator {
        val cornerRadiusProperties = "cornerRadius"
        return ObjectAnimator.ofFloat(this, cornerRadiusProperties, init, end)
    }

    private fun View.zoomInt(intrinsicWidth: Int)
        = valueAnimate(intrinsicWidth / RESIZE_BUTTON, intrinsicWidth.toFloat())

    private fun View.zoomOut(intrinsicWidth: Int)
        = valueAnimate(intrinsicWidth.toFloat(), intrinsicWidth / RESIZE_BUTTON)

    private fun View.valueAnimate(from: Float, to: Float): ValueAnimator {
        val containerView = this

        return ValueAnimator.ofFloat(from, to).apply {
            addUpdateListener { animation ->
                val scale = animation.animatedValue.toString().toFloat()
                containerView.scaleX = scale
                containerView.scaleY = scale
            }
        }
    }

    private fun createRectangle(): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.parseColor(BACKGROUND_COLOR))
            shape = GradientDrawable.RECTANGLE
        }
    }

}