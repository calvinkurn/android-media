package com.tokopedia.tokochat.util

import android.app.Activity
import android.os.Build
import android.view.Display

fun Activity?.isFromBubble(): Boolean {
    return try {
        this?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                isLaunchedFromBubble
            } else {
                val displayId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    display?.displayId
                } else {
                    @Suppress("DEPRECATION")
                    windowManager.defaultDisplay.displayId
                }
                displayId != Display.DEFAULT_DISPLAY
            }
        } ?: false
    } catch (ignored: Throwable) {
        false
    }
}
