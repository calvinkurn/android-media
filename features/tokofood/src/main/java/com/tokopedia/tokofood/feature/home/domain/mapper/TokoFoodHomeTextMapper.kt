package com.tokopedia.tokofood.feature.home.domain.mapper

import android.text.Spannable
import android.text.TextPaint
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import com.tokopedia.abstraction.common.utils.view.MethodChecker

object TokoFoodHomeTextMapper {

    fun removeUnderlineFromLink(text: String): Spannable {
        val spannable = MethodChecker.fromHtml(text) as Spannable
        for (u in spannable.getSpans(0, spannable.length, URLSpan::class.java)) {
            spannable.setSpan(object : UnderlineSpan() {
                override fun updateDrawState(tp: TextPaint) {
                    tp.isUnderlineText = false
                }
            }, spannable.getSpanStart(u), spannable.getSpanEnd(u), 0)
        }
        return spannable
    }
}