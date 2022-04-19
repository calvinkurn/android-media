package com.tokopedia.managename.view

import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.widget.TextView
import com.tokopedia.managename.R

object ViewUtil {
    fun stripUnderlines(textView: TextView) {
        val s: Spannable = SpannableString(textView.text)
        val spans = s.getSpans(0, s.length, URLSpan::class.java)
        spans.forEach {
            val start = s.getSpanStart(it)
            val end = s.getSpanEnd(it)
            s.removeSpan(it)
            val noUnderline = URLSpanNoUnderline(it.url)
            s.setSpan(noUnderline, start, end, 0)
            s.setSpan(ForegroundColorSpan(textView.context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)), start, end, 0)
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