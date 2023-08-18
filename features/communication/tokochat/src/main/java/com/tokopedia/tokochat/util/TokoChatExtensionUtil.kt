package com.tokopedia.tokochat.util

import android.app.Activity
import androidx.core.app.ActivityCompat

fun Activity?.isFromBubble(): Boolean {
    return try {
        this?.let {
            ActivityCompat.isLaunchedFromBubble(it)
        } == true
    } catch (ignored: Throwable) {
        false
    }
}
