package com.tokopedia.stickylogin.common.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.stickylogin.R

object StripedUnderlineUtil {

    fun stripUnderlines(textView: TextView) {
        val s: Spannable = SpannableString(textView.text)
        val spans = s.getSpans(0, s.length, URLSpan::class.java)
        for (span in spans) {
            var urlSpan = span
            val start = s.getSpanStart(urlSpan)
            val end = s.getSpanEnd(urlSpan)
            s.removeSpan(urlSpan)
            urlSpan = URLSpanNoUnderline(urlSpan.url)
            s.setSpan(urlSpan, start, end, 0)
            s.setSpan(ForegroundColorSpan(ContextCompat.getColor(textView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500)), start, end, 0)
        }
        textView.text = s
    }

    private class URLSpanNoUnderline(url: String?) : URLSpan(url) {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }
}