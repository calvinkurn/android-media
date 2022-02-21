package com.tokopedia.product.addedit.common.util

import android.view.View
import android.view.animation.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

private const val LAYOUT_HEIGHT_DEFAULT = 1
private const val INTERPOLATED_TIME_END = 1f
private const val ALPHA_VISIBLE = 1f
private const val ROTATED_DEGREE = 180f
private const val PIVOT_X_CENTER = 0.5f
private const val PIVOT_Y_CENTER = 0.5f

internal fun View?.animateExpand() = this?.run {
    val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(Int.ZERO, View.MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    layoutParams.height = LAYOUT_HEIGHT_DEFAULT
    show()
    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            layoutParams.height = if (interpolatedTime == INTERPOLATED_TIME_END) {
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            } else (targetHeight * interpolatedTime).toInt()
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
            if (interpolatedTime == INTERPOLATED_TIME_END) {
                gone()
            } else {
                layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                alpha = ALPHA_VISIBLE - interpolatedTime
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
    animateRotate(Float.ZERO, ROTATED_DEGREE)
}

internal fun View?.animateRotateCcw() = this?.run {
    animateRotate(ROTATED_DEGREE, Float.ZERO)
}

internal fun View?.animateRotate(fromDegree: Float, toDegree: Float) = this?.run {
    val duration = resources.getInteger(com.tokopedia.unifyprinciples.R.integer.Unify_T2).toLong()
    val rotate = RotateAnimation(fromDegree, toDegree, Animation.RELATIVE_TO_SELF, PIVOT_X_CENTER,
            Animation.RELATIVE_TO_SELF, PIVOT_Y_CENTER)

    rotate.duration = duration
    rotate.interpolator = LinearInterpolator()
    rotate.fillAfter = true
    startAnimation(rotate)
}