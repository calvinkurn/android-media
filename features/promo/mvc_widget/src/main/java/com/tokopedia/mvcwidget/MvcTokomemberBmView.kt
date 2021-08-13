package com.tokopedia.mvcwidget

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.FrameLayout
import androidx.core.view.ViewCompat

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

fun expand(view: View, iconBackgroundContainer: FrameLayout) {
    val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((view.parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    view.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = view.measuredHeight

    view.layoutParams.height = 1
    view.visibility = View.VISIBLE
    val animation: Animation = object : Animation() {
         override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            view.layoutParams.height =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            view.requestLayout()

             val marginLayoutParams = iconBackgroundContainer.layoutParams as ViewGroup.MarginLayoutParams
             marginLayoutParams.bottomMargin =
                 iconBackgroundContainer.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl4)
             iconBackgroundContainer.layoutParams = marginLayoutParams
             iconBackgroundContainer.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    animation.duration = ((targetHeight / view.context.resources.displayMetrics.density)).toLong()
    view.startAnimation(animation)
}

fun collapse(view: View, iconBackgroundContainer: FrameLayout) {
    val initialHeight = view.measuredHeight
    val animation: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                view.visibility = View.GONE
            } else {
                view.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                view.requestLayout()
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

    animation.duration = (((initialHeight / view.context.resources.displayMetrics.density)).toLong())
    view.startAnimation(animation)
}

data class MvcTokomemberBmViewData(val imageUrls: List<String>, val messages: List<String>, val buttonText: String)