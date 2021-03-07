package com.tokopedia.product.addedit.common.util

import android.view.View
import android.view.animation.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show


internal fun View?.animateExpand() = this?.run {
    val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    layoutParams.height = 1
    show()
    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            layoutParams.height = if (interpolatedTime == 1f) ConstraintLayout.LayoutParams.WRAP_CONTENT
            else (targetHeight * interpolatedTime).toInt()
            alpha = interpolatedTime
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    animation.duration = resources.getInteger(com.tokopedia.unifyprinciples.R.integer.Unify_T2).toLong()
    startAnimation(animation)
}

internal fun View?.animateCollapse() = this?.run {
    val initialHeight = measuredHeight
    val animation: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                gone()
            } else {
                layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                alpha = 1.0f - interpolatedTime
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    animation.interpolator = AccelerateDecelerateInterpolator()
    animation.duration = resources.getInteger(com.tokopedia.unifyprinciples.R.integer.Unify_T2).toLong()
    startAnimation(animation)
}

internal fun View?.animateRotateCw() = this?.run {
    animateRotate(0f, 180f)
}

internal fun View?.animateRotateCcw() = this?.run {
    animateRotate(180f, 0f)
}

internal fun View?.animateRotate(fromDegree: Float, toDegree: Float) = this?.run {
    val duration = resources.getInteger(com.tokopedia.unifyprinciples.R.integer.Unify_T2).toLong()
    val rotate = RotateAnimation(fromDegree, toDegree, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f)

    rotate.duration = duration
    rotate.interpolator = LinearInterpolator()
    rotate.fillAfter = true
    startAnimation(rotate)
}