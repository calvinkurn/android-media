package com.tokopedia.kotlin.extensions.view

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.view.ViewTreeObserver.OnWindowAttachListener

private var lastPixelHeight = 0
private var lastPercentageWidht = 0
private var lastPercentageHeight = 0
private const val minHorizontalPercentage = 0
private const val maxHorizontalPercentage = 101
private const val minVerticalPercentage = 0
private const val maxVerticalPercentage = 101
private val duplicateEnabled = false
const val TOP = 1
const val BOTTOM = 3
const val RIGHT = 2
const val LEFT = 4
const val LEFT_AND_RIGHT = 5
const val TOP_AND_BOTTOM = 6
const val NOWHERE = 7

fun View.setOnVisibilityChanged(onChanged: (height: Int, width: Int) -> Unit, onAttached:() -> Unit, onDetached:() -> Unit) {
    viewTreeObserver.addOnScrollChangedListener { object : OnScrollChangedListener {
        override fun onScrollChanged() {
            calculateVisibility(this@setOnVisibilityChanged, onChanged)
        }
    } }

    viewTreeObserver.addOnWindowAttachListener(object : OnWindowAttachListener{
        override fun onWindowAttached() {
            Log.d("VISIBILITY", "onWindowAttached")
            onAttached.invoke()
        }

        override fun onWindowDetached() {
            Log.d("VISIBILITY", "onWindowDetached")
            onDetached.invoke()
        }
    })
}


private fun calculateVisibility(v: View, onChanged: (height: Int, width: Int) -> Unit) {
    val rectf = Rect()
    v.getLocalVisibleRect(rectf)
    val top = rectf.top
    val bottom = rectf.bottom
    val right = rectf.right
    val left = rectf.left
    val width: Int = v.getWidth()
    val height: Int = v.getHeight()
    var heightPercentage: Int
    var heightPixels: Int
    var widthPercentage: Int
    var widthPixels: Int
    heightPixels = height + top - bottom
    widthPixels = width + left - right
    heightPercentage = (100 - heightPixels.toDouble() / height * 100).toInt()
    widthPercentage = (100 - widthPixels.toDouble() / width * 100).toInt()
    if (top > height || bottom > height) {
        heightPercentage = 0
    }
    if (right > width || left > width) {
        widthPercentage = 0
    }
    if (isBetweenHorizontalPercentageLimits(widthPercentage) && isBetweenVerticalPercentageLimits(
            heightPercentage
        )
    ) {
        if (duplicateEnabled || !duplicateEnabled && (lastPercentageHeight != heightPercentage || lastPercentageWidht != widthPercentage)) {
            lastPercentageHeight = heightPercentage
            lastPercentageWidht = widthPercentage
            onChanged(heightPercentage, widthPercentage)
        }
    }
}

private fun isBetweenHorizontalPercentageLimits(a: Int): Boolean {
    return if (a <= maxHorizontalPercentage && a >= minHorizontalPercentage) {
        true
    } else {
        false
    }
}


private fun isBetweenVerticalPercentageLimits(a: Int): Boolean {
    return if (a <= maxVerticalPercentage && a >= minVerticalPercentage) {
        true
    } else {
        false
    }
}
