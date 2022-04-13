package com.tokopedia.reviewcommon.extension

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View

fun View.generateHapticFeedback() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        performHapticFeedback(HapticFeedbackConstants.CONFIRM)
    } else {
        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }
}