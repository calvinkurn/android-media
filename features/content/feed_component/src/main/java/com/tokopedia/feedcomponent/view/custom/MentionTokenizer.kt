package com.tokopedia.feedcomponent.view.custom

import android.text.SpannableString
import android.widget.MultiAutoCompleteTextView
import com.tokopedia.feedcomponent.util.MentionTextHelper

/**
 * Created by jegul on 2019-08-05.
 */

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

        /**
         * Move Cursor back until first occurence of '@'
         */
        while (movingTempCursor > 0 && text[movingTempCursor - 1] != '@') {
            movingTempCursor--
        }

        val spannableString = SpannableString(text)
        val allMentionSpans = MentionTextHelper.getAllMentionSpansFromText(spannableString)
        val isPreviousAlreadySpanned = allMentionSpans.any { (movingTempCursor - 1) >= it.start && (movingTempCursor - 1) <= it.end }

        return if (
                movingTempCursor < 1
                || text[movingTempCursor - 1] != '@'
                || isPreviousAlreadySpanned
        ) cursor
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
            MentionTextHelper.createMentionTag(text)
        }
    }
}