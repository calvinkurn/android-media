package com.tokopedia.kotlin.extensions.view

import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.utils.scroll.RecyclerViewScrollListener

/**
 * Perform a smooth snap scroll to the specified RecyclerView item position. When we provide a non-zero
 * [topOffset] and use [snapMode] [LinearSmoothScroller.SNAP_TO_START] then the UI will look like this:
 * ------------------------------
 * |   item at [position] - 1   |
 * |   item at [position]       |
 * |   item at [position] + 1   |
 * |   item at [position] + 2   |
 * |   item at [position] + 3   |
 * |   item at [position] + 4   |
 * ------------------------------
 * The item above [position] will be visible partially/fully visible depending on how much [topOffset]
 * we provide and how much the height of the item above the [position]
 *
 * @param position The position of the item we want to scroll into
 * @param snapMode The snap mode preference must be any of the option provided on [LinearSmoothScroller]
 * @param topOffset The top offset to add to the top of the item we want to scroll into.
 * @param millisecondsPerInch The speed of the smooth scroller. The default is the same as the [LinearSmoothScroller.MILLISECONDS_PER_INCH]
 */
fun RecyclerView.smoothSnapToPosition(
    position: Int,
    snapMode: Int = LinearSmoothScroller.SNAP_TO_START,
    topOffset: Int = Int.ZERO,
    millisecondsPerInch: Float = 25f
) {
    val smoothScroller = object : LinearSmoothScroller(this.context) {
        override fun calculateDyToMakeVisible(
            view: View?,
            snapPreference: Int
        ): Int = super.calculateDyToMakeVisible(view, snapPreference) + topOffset
        override fun calculateSpeedPerPixel(
            displayMetrics: DisplayMetrics
        ): Float = millisecondsPerInch / displayMetrics.densityDpi;
        override fun getVerticalSnapPreference(): Int = snapMode
        override fun getHorizontalSnapPreference(): Int = snapMode
    }
    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}

fun RecyclerView.attachOnScrollListener(onScrollDown: () -> Unit, onScrollUp: () -> Unit) {
    val scrollListener = RecyclerViewScrollListener(onScrollDown, onScrollUp)
    addOnScrollListener(scrollListener)
}
