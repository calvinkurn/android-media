package com.tokopedia.kol.feature.comment.view.tokenizer

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.widget.MultiAutoCompleteTextView

class MentionTokenizer : MultiAutoCompleteTextView.Tokenizer {

    override fun findTokenEnd(text: CharSequence?, cursor: Int): Int {
        if (text == null) return 0

        var movingTempCursor = cursor
        val textLength = text.length

        while (movingTempCursor < textLength) {
            if (text[movingTempCursor] == ' ') {
                return movingTempCursor
            } else {
                movingTempCursor++
            }
        }

        return textLength
    }

    override fun findTokenStart(text: CharSequence?, cursor: Int): Int {
        if (text == null) return 0

        var movingTempCursor = cursor

        while (movingTempCursor > 0 && text[movingTempCursor - 1] != '@') {
            movingTempCursor--
        }

        return if (movingTempCursor < 1 || text[movingTempCursor - 1] != '@') cursor
        else movingTempCursor
    }

    override fun terminateToken(text: CharSequence?): CharSequence {
        if (text == null) return ""

        var length = text.length

        while (length > 0 && text[length - 1] == ' ') {
            length--
        }

        return if (length > 0 && text[length - 1] == ' ') {
            text
        } else {
            if (text is Spanned) {
                val sp = SpannableString("$text ")
                TextUtils.copySpansFrom(text, 0, text.length, Any::class.java, sp, 0)
                sp
            } else {
                SpannableString("$text ").apply {
                    setSpan(ForegroundColorSpan(Color.RED), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }
}