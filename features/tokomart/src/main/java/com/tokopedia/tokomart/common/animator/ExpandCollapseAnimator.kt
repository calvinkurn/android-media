package com.tokopedia.tokomart.common.animator

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.animation.TranslateAnimation

class ExpandCollapseAnimator(
    private val view: View,
    private val expandListener: Animation.AnimationListener? = null,
    private val collapseListener: Animation.AnimationListener? = null
) {

    private val animDuration by lazy {
        view.context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
    }

    private val targetHeight by lazy {
        val width = View.MeasureSpec.makeMeasureSpec((view.parent as View).width, View.MeasureSpec.EXACTLY)
        val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(width, height)
        view.measuredHeight
    }

    fun expand() {
        view.run {
            val initialHeight = 1
            val measuredHeight = targetHeight

            layoutParams.height = initialHeight
            visibility = View.VISIBLE

            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    layoutParams.height = if (interpolatedTime == 1f) {
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    } else {
                        (measuredHeight * interpolatedTime).toInt()
                    }
                    requestLayout()
                }

                override fun willChangeBounds(): Boolean = true
            }.apply {
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {
                        expandListener?.onAnimationRepeat(animation)
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        val translateAnimation = TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f).apply {
                            duration = 1
                        }
                        startAnimation(translateAnimation)
                        expandListener?.onAnimationEnd(animation)
                    }

                    override fun onAnimationStart(animation: Animation?) {
                        expandListener?.onAnimationStart(animation)
                    }
                })
                duration = animDuration
            }
            startAnimation(animation)
        }
    }

    fun collapse() {
        view.run {
            val initialHeight = measuredHeight
            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    if (interpolatedTime == 1f) {
                        visibility = View.GONE
                    } else {
                        layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                        requestLayout()
                    }
                }

                override fun willChangeBounds(): Boolean = true
            }.apply {
                setAnimationListener(collapseListener)
                duration = animDuration
            }
            startAnimation(animation)
        }
    }
}