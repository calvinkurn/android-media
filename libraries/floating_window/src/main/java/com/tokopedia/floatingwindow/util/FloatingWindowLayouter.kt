package com.tokopedia.floatingwindow.util

import android.view.View
import android.view.WindowManager
import com.tokopedia.floatingwindow.FloatingWindow

/**
 * Created by jegul on 27/11/20
 */
class FloatingWindowLayouter internal constructor(
        val view: View,
        val layoutParams: WindowManager.LayoutParams
) {

    private val floatingWindow: FloatingWindow
        get() = FloatingWindow.getInstance(view.context)

    fun updatePosition(x: Int, y: Int) {
        floatingWindow.getWindowManager().updateViewLayout(
                view,
                layoutParams.apply {
                    this.x = x
                    this.y = y
                }
        )
    }

    fun updateSize(width: Int, height: Int) {
        floatingWindow.getWindowManager().updateViewLayout(
                view,
                layoutParams.apply {
                    this.width = width
                    this.height = height
                }
        )
    }
}