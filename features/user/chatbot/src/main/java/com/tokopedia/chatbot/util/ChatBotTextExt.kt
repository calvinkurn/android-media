package com.tokopedia.chatbot.util

import android.text.Spannable
import android.text.TextPaint
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker


fun TextView.removeUnderLineFromLinkAndSetText(text: String) {
    val spannable = MethodChecker.fromHtml(text) as Spannable
    for (u in spannable.getSpans(0, spannable.length, URLSpan::class.java)) {
        spannable.setSpan(object : UnderlineSpan() {
            override fun updateDrawState(tp: TextPaint) {
                tp.isUnderlineText = false
            }
        }, spannable.getSpanStart(u), spannable.getSpanEnd(u), 0)
    }
    this.text = spannable
}
