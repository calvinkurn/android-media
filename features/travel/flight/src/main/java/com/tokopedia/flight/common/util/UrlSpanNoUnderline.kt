package com.tokopedia.flight.common.util

import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.URLSpan
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.ZERO

internal class URLSpanNoUnderline(url: String) : URLSpan(url) {

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }

    companion object {

        fun stripUnderlines(textView: TextView) {
            val s: Spannable = SpannableString(textView.text)
            val spans = s.getSpans(
                Int.ZERO, s.length,
                URLSpan::class.java
            )
            for (span in spans) {
                val start = s.getSpanStart(span)
                val end = s.getSpanEnd(span)
                s.removeSpan(span)
                s.setSpan(URLSpanNoUnderline(span.url), start, end, Int.ZERO)
            }
            textView.text = s
        }
    }
}
