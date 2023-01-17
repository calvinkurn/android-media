package com.tokopedia.content.common.util

import android.text.Spannable
import android.text.SpannableStringBuilder

/**
 * Created By : Jonathan Darwin on July 05, 2022
 */

fun SpannableStringBuilder.setSpanOnText(
    text: String,
    vararg spans: Any,
) {
    val start = indexOf(text)
    val end = start + text.length

    spans.forEach {
        setSpan(it, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}