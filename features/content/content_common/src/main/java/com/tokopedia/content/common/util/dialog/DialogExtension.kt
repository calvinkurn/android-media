package com.tokopedia.content.common.util.dialog

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import com.tokopedia.abstraction.common.utils.view.MethodChecker

/**
 * Created By : Jonathan Darwin on September 26, 2023
 */
@RequiresApi(Build.VERSION_CODES.M)
fun Dialog.updateNavigationBarColors(colorResArray: IntArray, useDarkIcon: Boolean = true) {
    setNavigationBarColors(colorResArray)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && useDarkIcon) setDarkNavigationIcon()
}

@RequiresApi(Build.VERSION_CODES.M)
fun Dialog.updateNavigationBarColor(@ColorRes colorRes: Int, useDarkIcon: Boolean = true) {
    updateNavigationBarColors(intArrayOf(colorRes), useDarkIcon)
}

@RequiresApi(Build.VERSION_CODES.M)
fun Dialog.setNavigationBarColors(colorResArray: IntArray) {
    val theWindow: Window? = window
    if (theWindow != null) {
        val metrics = android.util.DisplayMetrics()
        theWindow.windowManager.defaultDisplay.getMetrics(metrics)
        val dimDrawable = GradientDrawable()

        val drawableList = colorResArray.map {
            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.setColor(MethodChecker.getColor(context, it))
            return@map drawable
        }

        val layers = arrayOf<Drawable>(dimDrawable) + drawableList
        val windowBackground = LayerDrawable(layers)
        windowBackground.setLayerInsetTop(1, metrics.heightPixels)
        theWindow.setBackgroundDrawable(windowBackground)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Dialog.setDarkNavigationIcon() {
    val decorView = window?.decorView
    if (decorView != null) {
        decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
}
