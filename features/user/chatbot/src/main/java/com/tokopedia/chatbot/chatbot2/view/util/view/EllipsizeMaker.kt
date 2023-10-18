package com.tokopedia.chatbot.chatbot2.view.util.view

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chatbot.R
import java.util.*

object EllipsizeMaker {

    const val MESSAGE_LINE_COUNT = 10
    const val SUBSEQUENCE_TEXT_LENGTH = 3
    const val LINE_END_INDEX = 1

    fun getTruncatedMsg(message: TextView): CharSequence? {
        return if (message.layout != null) {
            val lineEndIndex = message.layout.getLineEnd(MESSAGE_LINE_COUNT - LINE_END_INDEX)
            String.format(
                Locale.getDefault(),
                "%s%s",
                message.text.subSequence(0, lineEndIndex - SUBSEQUENCE_TEXT_LENGTH),
                message.context.getString(
                    R.string.cb_bot_three_dots
                )
            )
        } else {
            message.text
        }
    }

    fun setTruncatedMsg(message: TextView, msg: String) {
        if (message.layout != null) {
            val lineEndIndex = message.layout.getLineEnd(MESSAGE_LINE_COUNT - LINE_END_INDEX)
            val text = removeUnderlineFromLink(msg)
            if (text.length < lineEndIndex) return
            val spannable = text.subSequence(0, lineEndIndex - SUBSEQUENCE_TEXT_LENGTH)
            val fText = (spannable as SpannableStringBuilder).append(message.context.getString(R.string.cb_bot_three_dots))
            message.text = fText
        }
    }

    fun removeUnderlineFromLink(text: String): Spannable {
        val spannable = MethodChecker.fromHtml(text) as Spannable
        for (u in spannable.getSpans(0, spannable.length, URLSpan::class.java)) {
            spannable.setSpan(
                object : UnderlineSpan() {
                    override fun updateDrawState(tp: TextPaint) {
                        tp.isUnderlineText = false
                    }
                },
                spannable.getSpanStart(u),
                spannable.getSpanEnd(u),
                0
            )
        }
        return spannable
    }
}
