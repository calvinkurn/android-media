package com.tokopedia.privacycenter.common.utils

import android.text.SpannableString
import android.text.TextPaint
import android.text.style.URLSpan
import com.tokopedia.unifyprinciples.Typography

fun Typography.removeUrlLine() {
    val spannable = SpannableString(text)
    for (u in spannable.getSpans(0, spannable.length, URLSpan::class.java)) {
        spannable.setSpan(
            object : URLSpan(u.url) {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            },
            spannable.getSpanStart(u),
            spannable.getSpanEnd(u),
            0
        )
    }
    text = spannable
}
