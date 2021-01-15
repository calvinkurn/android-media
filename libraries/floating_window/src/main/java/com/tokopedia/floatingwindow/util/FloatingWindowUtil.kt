@file:JvmName("FloatingWindowUtil")

package com.tokopedia.floatingwindow.util

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.view.View

/**
 * Created by jegul on 26/11/20
 */
fun Context.isDrawOverOtherAppsEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        true
    } else {
        Settings.canDrawOverlays(this)
    }
}

fun View.registerDraggableTouchListener(
        initialPosition: () -> Point,
        onDragged: (x: Int, y: Int) -> Unit
) {
    FloatingWindowTouchListener( this, initialPosition, onDragged)
}