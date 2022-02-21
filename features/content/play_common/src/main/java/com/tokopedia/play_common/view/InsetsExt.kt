package com.tokopedia.play_common.view

import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Created by jegul on 02/07/20
 */
/**
 * Updates this view's padding. This version of the method allows using named parameters
 * to just set one or more axes.
 *
 * @see View.setPadding
 */
fun View.updatePadding(
        @Px left: Int = paddingLeft,
        @Px top: Int = paddingTop,
        @Px right: Int = paddingRight,
        @Px bottom: Int = paddingBottom
) {
    setPadding(left, top, right, bottom)
}


/**
 * Updates the margins in the [ViewGroup]'s [ViewGroup.MarginLayoutParams].
 * This version of the method allows using named parameters to just set one or more axes.
 *
 * @see ViewGroup.MarginLayoutParams.setMargins
 */
fun ViewGroup.MarginLayoutParams.updateMargins(
        @Px left: Int = leftMargin,
        @Px top: Int = topMargin,
        @Px right: Int = rightMargin,
        @Px bottom: Int = bottomMargin
) {
    setMargins(left, top, right, bottom)
}

fun View.requestApplyInsetsWhenAttached() {
    val isAttached = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) isAttachedToWindow else false
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) requestApplyInsets()
        else requestFitSystemWindows()
    } catch (e: Exception) {}
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

data class InitialPadding(val left: Int, val top: Int,
                          val right: Int, val bottom: Int)

data class InitialMargin(val left: Int, val top: Int,
                         val right: Int, val bottom: Int)

private fun View.recordInitialPadding() = InitialPadding(
        paddingStart,
        paddingTop,
        paddingEnd,
        paddingBottom)

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
