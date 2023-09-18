package com.tokopedia.order_management_common.util

import android.graphics.Typeface
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.HapticFeedbackConstants
import android.view.View

fun View.generateHapticFeedback() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        performHapticFeedback(HapticFeedbackConstants.CONFIRM)
    } else {
        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }
}


fun String.stripLastDot(): String {
    return removeSuffix(".")
}

fun composeItalicNote(productNote: String): SpannableString {
    return SpannableString(productNote).apply {
        setSpan(StyleSpan(Typeface.ITALIC), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}
