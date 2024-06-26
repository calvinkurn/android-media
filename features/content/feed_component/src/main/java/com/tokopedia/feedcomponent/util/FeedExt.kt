package com.tokopedia.feedcomponent.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.style.StyleSpan

/**
 * @author by astidhiyaa on 12/10/22
 */
inline fun buildSpannedString(builderAction: SpannableStringBuilder.() -> Unit): SpannedString {
    val builder = SpannableStringBuilder()
    builder.builderAction()
    return SpannedString(builder)
}

inline fun SpannableStringBuilder.inSpans(
    span: Any,
    builderAction: SpannableStringBuilder.() -> Unit
): SpannableStringBuilder {
    val start = length
    builderAction()
    setSpan(span, start, length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    return this
}

inline fun SpannableStringBuilder.bold(
    builderAction: SpannableStringBuilder.() -> Unit
): SpannableStringBuilder = inSpans(StyleSpan(Typeface.BOLD), builderAction = builderAction)

fun SpannableString.safeSetSpan(what: Any, start: Int, end: Int, flags: Int) {
    try {
        setSpan(what, start, end, flags)
    } catch (throwable: Throwable) {
        // ignore styling
    }
}
