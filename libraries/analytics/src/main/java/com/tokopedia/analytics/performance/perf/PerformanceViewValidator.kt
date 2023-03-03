package com.tokopedia.analytics.performance.perf

import android.content.res.Resources
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun View.setOnViewFillingViewport(onViewFillingViewport: () -> Unit) {
    when (this) {
        is RecyclerView -> {
            if (isChildFillingViewport()) {
                onViewFillingViewport.invoke()
            }
        }
        else -> {
            if (isViewFillingViewport()) {
                onViewFillingViewport.invoke()
            }
        }
    }
}

fun View.heightIsFillingViewport(): Boolean {
    val viewportSize = getScreenHeight()
    Log.d("DevaraFikry", "Class: ${this.javaClass.simpleName}")

    Log.d("DevaraFikry", "Height: ${this.height}")
    val threshold = viewportSize * 0.75
    return this.height >= threshold
}

private fun View.isViewFillingViewport(): Boolean {
    val height = this.height
    val threshold = getScreenHeight() * 0.75

    return height > threshold
}

private fun RecyclerView.isChildFillingViewport(): Boolean {
    val totalHeight = calculateTotalChildHeight()
    val threshold = getScreenHeight() * 0.75

    return totalHeight > threshold
}

private fun RecyclerView.calculateTotalChildHeight(): Int {
    var totalHeight = 0
    for (i in 0 until this.childCount) {
        totalHeight += this.getChildAt(i).height
    }
    return totalHeight
}

private fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

private fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}
