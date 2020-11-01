package com.tokopedia.officialstore.category.presentation.widget

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
/**
 * Created by Lukas on 31/10/20.
 */
inline fun getValueAnimator(
        startValue: Float,
        endValue: Float,
        duration: Long,
        interpolator: TimeInterpolator,
        crossinline updateListener: (progress: Float) -> Unit
): ValueAnimator {
    val a = ValueAnimator.ofFloat(startValue, endValue)
    a.addUpdateListener { updateListener(it.animatedValue as Float) }
    a.duration = duration
    a.interpolator = interpolator
    return a
}


inline val Int.dp: Int
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()
inline val Float.dp: Float
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

inline val Context.screenWidth: Int
    get() = Point().also { (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(it) }.x

inline val View.screenWidth: Int
    get() = context!!.screenWidth

inline fun View.afterMeasured(crossinline f: View.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredHeight > 0 && measuredWidth > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}