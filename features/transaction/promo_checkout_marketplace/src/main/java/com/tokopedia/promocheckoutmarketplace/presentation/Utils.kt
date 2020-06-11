package com.tokopedia.promocheckoutmarketplace.presentation

import android.app.Activity
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.pxToDp

const val IMAGE_ALPHA_DISABLED = 128
const val IMAGE_ALPHA_ENABLED = 255

fun setImageFilterGrayScale(imageView: ImageView) {
    val matrix = ColorMatrix()
    matrix.setSaturation(0f)
    val disabledColorFilter = ColorMatrixColorFilter(matrix)
    imageView.colorFilter = disabledColorFilter
    imageView.imageAlpha = IMAGE_ALPHA_DISABLED
}

fun setImageFilterNormal(imageView: ImageView) {
    imageView.colorFilter = null
    imageView.imageAlpha = IMAGE_ALPHA_ENABLED
}

fun getKeyboardHeight(view: View): Int {
    val heightDiff = view.rootView?.height?.minus(view.height) ?: 0
    val displayMetrics = DisplayMetrics()
    val windowManager = view.context?.applicationContext?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    val heightDiffInDp = heightDiff.pxToDp(displayMetrics)
    if (heightDiffInDp > 100) {
        return heightDiff
    }
    return 0
}

fun getDeviceHeight(activity: Activity): Int {
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}
