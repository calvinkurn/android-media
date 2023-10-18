package com.tokopedia.content.common.util

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.SpannedString

/**
 * Created By : Jonathan Darwin on July 05, 2022
 */
inline fun buildSpannedString(builderAction: SpannableStringBuilder.() -> Unit): SpannedString {
    val builder = SpannableStringBuilder()
    builder.builderAction()
    return SpannedString(builder)
}

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
