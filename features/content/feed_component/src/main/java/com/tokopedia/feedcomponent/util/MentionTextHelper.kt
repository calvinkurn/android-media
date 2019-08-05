package com.tokopedia.feedcomponent.util

import android.text.Spannable
import android.text.SpannableStringBuilder
import com.tokopedia.feedcomponent.view.span.MentionSpan
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * $USER_ID -> the ID of mentioned User
 * $FULL_NAME -> the full name of mentioned user
 *
 * From Tokenizer -> <mention>{@$USER_ID|$FULL_NAME@}</mention>
 * In Text -> @<mention>{@$USER_ID|$FULL_NAME@}</mention>
 * Expected to be sent and received from Server -> {@$USER_ID|$FULL_NAME@}
 *
 */
object MentionTextHelper {
    private const val MENTION_CHAR = "@"
    private const val MENTION_TAG = "mention"
    private const val OPENING_MENTION_TAG = "<$MENTION_TAG>"
    private const val CLOSING_MENTION_TAG = "</$MENTION_TAG>"

    private const val ID_NAME_DELIMITER = "|"

    private const val ALLOWED_CHARS_REGEX = "[A-Za-z0-9-_ ]"
    private const val TOKENIZER_FULL_EDIT_REGEX = "($MENTION_CHAR$OPENING_MENTION_TAG\\{(@[0-9]+\\$ID_NAME_DELIMITER$ALLOWED_CHARS_REGEX+@)?\\}$CLOSING_MENTION_TAG)"
    private const val TOKENIZER_CONTENT_ONLY_REGEX = "(?<=($MENTION_CHAR)?$OPENING_MENTION_TAG\\{)(@[0-9]+\\$ID_NAME_DELIMITER$ALLOWED_CHARS_REGEX+@)(?=\\}$CLOSING_MENTION_TAG)"
    private val tokenizerFullPattern = Pattern.compile(TOKENIZER_FULL_EDIT_REGEX)
    private val tokenizerContentOnlyPattern = Pattern.compile(TOKENIZER_CONTENT_ONLY_REGEX)

    /**
     * Create mention tag with format <mention>$params</mention>
     * @param mentioned the text to be enclosed by <mention> tag
     */
    fun createMentionTag(mentioned: CharSequence): CharSequence {
        return "$OPENING_MENTION_TAG$mentioned$CLOSING_MENTION_TAG"
    }

    fun spanText(text: CharSequence, mentionColor: Int): Spannable {
        val spannableString = SpannableStringBuilder(text)
        val matcher = tokenizerFullPattern.matcher(text)
        while (matcher.find()) {
            val mentionSpan = getMentionSpanFromTag(matcher.group(), mentionColor)
            if (mentionSpan != null) {
                spannableString.setSpan(mentionSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableString.replace(matcher.start() + 1, matcher.end(), mentionSpan.fullName)
            }
        }

        return spannableString
    }

    private fun getMentionSpanFromTag(tag: String, mentionColor: Int): MentionSpan? {
        val user = getMentionableUserViewModelFromTokenizerText(tag)
        return user?.let {
            MentionSpan(
                    color = mentionColor,
                    fullText = user.toString(),
                    fullName = user.fullName,
                    userId = user.id
            )
        }
    }

    fun getAllMentionSpansFromText(text: Spannable): Array<MentionSpan> {
        return text.getSpans(0, text.length, MentionSpan::class.java)
    }

    fun stripInvalidMentionFromText(text: Spannable, existingSpanList: Iterable<MentionSpan>): Spannable {
        val spannableStringBuilder = SpannableStringBuilder(text)
        existingSpanList.forEach { span ->
            val spanStart = text.getSpanStart(span)
            val spanEnd = text.getSpanEnd(span)
            if (spanEnd - spanStart - 1 != span.length) {
                spannableStringBuilder.replace(spanStart, spanEnd, "")
            }
        }

        return spannableStringBuilder
    }

    private fun getMentionableUserViewModelFromTokenizerText(text: String): MentionableUserViewModel? {
        val contentMatcher: Matcher = tokenizerContentOnlyPattern.matcher(text)
        return if (contentMatcher.find()) {
            val content = contentMatcher.group()
            val splittedContent = content.split(ID_NAME_DELIMITER)
            val userId: String = splittedContent.first().replace(MENTION_CHAR, "")
            val fullName: String = splittedContent.last().replace(MENTION_CHAR, "")

            MentionableUserViewModel(
                    id = userId,
                    userName = "",
                    fullName = fullName,
                    imageUrl = ""
            )
        } else null
    }

    fun deSpanMentionTag(text: Spannable): String {
        val spannableStringBuilder = SpannableStringBuilder(text)
        val currentSpanList = getAllMentionSpansFromText(text)
        currentSpanList.forEach { span ->
            val spanStart = spannableStringBuilder.getSpanStart(span)
            val spanEnd = spannableStringBuilder.getSpanEnd(span)
            try {
                spannableStringBuilder.replace(spanStart, spanEnd, span.fullText)
            } catch (e: Exception) { }
        }

        return spannableStringBuilder.toString()
    }
}