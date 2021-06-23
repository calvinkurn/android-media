package com.tokopedia.mvcwidget

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.FrameLayout
import androidx.core.view.ViewCompat

const val COLLAPSES_SPEED = 0.5

fun View.setMargin(left: Int, top: Int, right: Int, bottom: Int) {
    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(left, top, right, bottom)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        layoutParams.marginStart = left
        layoutParams.marginEnd = right
    }
}

inline fun View.doOnLayout(crossinline action: (view: View) -> Unit) {
    if (ViewCompat.isLaidOut(this) && !isLayoutRequested) {
        action(this)
    } else {
        doOnNextLayout {
            action(it)
        }
    }
}

inline fun View.doOnNextLayout(crossinline action: (view: View) -> Unit) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
                view: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
        ) {
            view.removeOnLayoutChangeListener(this)
            action(view)
        }
    })
}

fun expand(v: View, iconBackgroundContainer: FrameLayout) {
    val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = v.measuredHeight

    v.layoutParams.height = 1
    v.visibility = View.VISIBLE
    val a: Animation = object : Animation() {
         override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            v.layoutParams.height =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            v.requestLayout()

             val marginLayoutParams = iconBackgroundContainer.layoutParams as ViewGroup.MarginLayoutParams
             marginLayoutParams.bottomMargin =
                 iconBackgroundContainer.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
             iconBackgroundContainer.layoutParams = marginLayoutParams
             iconBackgroundContainer.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    a.duration = ((targetHeight / v.context.resources.displayMetrics.density) * COLLAPSES_SPEED).toLong()
    v.startAnimation(a)
}

fun collapse(v: View, iconBackgroundContainer: FrameLayout) {
    val initialHeight = v.measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                v.visibility = View.GONE
            } else {
                v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                v.requestLayout()

                val marginLayoutParams = iconBackgroundContainer.layoutParams as ViewGroup.MarginLayoutParams
                marginLayoutParams.bottomMargin =
                    iconBackgroundContainer.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0)
                iconBackgroundContainer.layoutParams = marginLayoutParams
                iconBackgroundContainer.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    a.duration = (((initialHeight / v.context.resources.displayMetrics.density)).toLong())
    v.startAnimation(a)
}

data class MvcTokomemberBmViewData(val imageUrls: List<String>, val messages: List<String>, val buttonText: String)