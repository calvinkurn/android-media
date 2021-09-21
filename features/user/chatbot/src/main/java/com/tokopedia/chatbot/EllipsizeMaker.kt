package com.tokopedia.chatbot

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker

object EllipsizeMaker {

    const val MESSAGE_LINE_COUNT = 10

    fun getTruncatedMsg(message: TextView): CharSequence? {
        return if (message.layout != null) {
            val lineEndIndex = message.layout.getLineEnd(MESSAGE_LINE_COUNT - 1)
            String.format("%s%s", message.text.subSequence(0, lineEndIndex - 3), message.context.getString(R.string.cb_bot_three_dots))
        } else {
            message.text
        }
    }

    fun setTruncatedMsg(message: TextView, msg: String) {
        if (message.layout != null) {
            val lineEndIndex = message.layout.getLineEnd(MESSAGE_LINE_COUNT - 1)
            val text = removeUnderlineFromLink(msg)
            if (text.length < lineEndIndex) return
            val spannable = text.subSequence(0, lineEndIndex - 3)
            val fText = (spannable as SpannableStringBuilder).append(message.context.getString(R.string.cb_bot_three_dots))
            message.text = fText
        }
    }

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