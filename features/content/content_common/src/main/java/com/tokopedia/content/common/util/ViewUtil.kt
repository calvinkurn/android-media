package com.tokopedia.content.common.util

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Created by kenny.hadisaputra on 09/10/23
 */
val View.marginLp: ViewGroup.MarginLayoutParams
    get() = layoutParams as ViewGroup.MarginLayoutParams

fun View.requestApplyInsetsWhenAttached() {
    val isAttached = isAttachedToWindow
    if (isAttached) {
        // We're already attached, just request as normal
        invalidateInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.invalidateInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun View.invalidateInsets() {
    try {
        requestApplyInsets()
    } catch (_: Exception) {
        return
    }
}

fun View.doOnApplyWindowInsets(f: (view: View, insets: WindowInsetsCompat, padding: InitialPadding, margin: InitialMargin) -> Unit) {
    val initialPadding = recordInitialPadding()
    val initialMargin = recordInitialMargin()

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, initialPadding, initialMargin)
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

data class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)

data class InitialMargin(val left: Int, val top: Int, val right: Int, val bottom: Int)

private fun View.recordInitialPadding() = InitialPadding(
    paddingStart,
    paddingTop,
    paddingEnd,
    paddingBottom
)

private fun View.recordInitialMargin(): InitialMargin {
    val margin = layoutParams as? ViewGroup.MarginLayoutParams
        ?: return InitialMargin(0, 0, 0, 0)

    return InitialMargin(
        margin.marginStart,
        margin.topMargin,
        margin.marginEnd,
        margin.bottomMargin
    )
}

inline fun View.changeConstraint(transform: ConstraintSet.() -> Unit) {
    require(this is ConstraintLayout)

    val constraintSet = ConstraintSet()

    constraintSet.clone(this)
    constraintSet.transform()
    constraintSet.applyTo(this)
}

