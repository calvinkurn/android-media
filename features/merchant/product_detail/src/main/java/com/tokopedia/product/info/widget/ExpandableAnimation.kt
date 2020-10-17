package com.tokopedia.product.info.widget

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * Created by Yehezkiel on 16/10/20
 */
object ExpandableAnimation {
    fun expand(view: View, onAnimationEndListener: (() -> Unit)? = null) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.AT_MOST)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        if (view is ViewGroup) {
            view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        } else {
            view.measure(widthSpec, heightSpec)
        }

        val actualheight = view.measuredHeight

        view.layoutParams.height = 0
        view.visibility = View.VISIBLE

        val animation: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                view.layoutParams.height = if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (actualheight * interpolatedTime).toInt()
                view.requestLayout()
            }
        }

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                onAnimationEndListener?.invoke()
            }

            override fun onAnimationStart(p0: Animation?) {}
        })

        animation.duration = (actualheight / view.context.resources.displayMetrics.density).toLong()
        view.startAnimation(animation)
    }

    fun collapse(view: View) {
        val actualHeight = view.measuredHeight

        val animation: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    view.visibility = View.GONE
                } else {
                    view.layoutParams.height = actualHeight - (actualHeight * interpolatedTime).toInt()
                    view.requestLayout()
                }
            }
        }
        animation.duration = (actualHeight / view.context.resources.displayMetrics.density).toLong()
        view.startAnimation(animation)
    }
}