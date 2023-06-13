package com.tokopedia.shop.search.util

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan

data class SpanTextHelper(
    var sourceString: String
) : SpannableString(sourceString) {

    fun bold(targetString: String, isReverse: Boolean, isIgnoreCase: Boolean): SpanTextHelper {
        val startIndex = sourceString.indexOf(targetString, ignoreCase = isIgnoreCase)
        val endIndex = startIndex + targetString.length
        if (startIndex == -1)
            return this
        if (isReverse) {
            setSpan(StyleSpan(Typeface.BOLD), 0, startIndex, SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), endIndex, sourceString.length, SPAN_INCLUSIVE_EXCLUSIVE)
        } else {
            setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, SPAN_INCLUSIVE_EXCLUSIVE)
        }
        return this
    }
}
