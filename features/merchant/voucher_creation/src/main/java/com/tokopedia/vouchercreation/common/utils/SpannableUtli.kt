package com.tokopedia.vouchercreation.common.utils

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View

fun getSpannableString(color: Int,
                       fullString: String,
                       spanString: String,
                       onSpanClicked: () -> Unit) : SpannableString {

    val spannableString = SpannableString(fullString)
    val spannableStringColor = ForegroundColorSpan(color)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onSpanClicked()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false
        }
    }

    val start = fullString.indexOf(string = spanString, ignoreCase = true)
    val end = start.plus(spanString.length)
    return spannableString.apply {
        setSpan(spannableStringColor, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        setSpan(clickableSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}