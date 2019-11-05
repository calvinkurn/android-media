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

    /**
     * Condition to show list of suggestions:
     * 1. When detected '@' then followed by minimum 3 words
     * 2. Previous words should not already been spanned
     */
    override fun findTokenStart(text: CharSequence?, cursor: Int): Int {
        if (text == null) return 0

        var movingTempCursor = cursor

        /**
         * Search for '@'
         */
        while (movingTempCursor > 0 && text[movingTempCursor - 1] != '@') {
            movingTempCursor--
        }

        val spannableString = SpannableString(text)
        val allMentionSpans = MentionTextHelper.getRenewedMentionSpans(spannableString)
        val isPreviousAlreadySpanned =
                allMentionSpans
                        .asSequence()
                        .filter { it.start <= movingTempCursor }
                        .any { (movingTempCursor - 1) <= it.end }

        return if (movingTempCursor > 0 &&
                text[movingTempCursor - 1] == '@' &&
                !isPreviousAlreadySpanned) movingTempCursor else cursor
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