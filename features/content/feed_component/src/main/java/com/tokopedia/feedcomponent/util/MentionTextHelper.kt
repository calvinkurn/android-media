package com.tokopedia.feedcomponent.util

import android.support.annotation.ColorInt
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
    const val MENTION_CHAR = "@"
    private const val MENTION_TAG = "mention"
    private const val OPENING_MENTION_TAG = "<$MENTION_TAG>"
    private const val CLOSING_MENTION_TAG = "</$MENTION_TAG>"

    private const val ID_NAME_DELIMITER = "|"

    private const val ALLOWED_CHARS_REGEX = "[A-Za-z0-9-_ ]"
    private const val TOKENIZER_FULL_EDIT_REGEX = "($MENTION_CHAR$OPENING_MENTION_TAG\\{(@[0-9]+\\$ID_NAME_DELIMITER$ALLOWED_CHARS_REGEX+@)?\\}$CLOSING_MENTION_TAG)"
    private const val TOKENIZER_CONTENT_ONLY_REGEX = "(?<=($MENTION_CHAR)?$OPENING_MENTION_TAG\\{)(@[0-9]+\\$ID_NAME_DELIMITER$ALLOWED_CHARS_REGEX+@)?(?=\\}$CLOSING_MENTION_TAG)"

    private const val READ_FULL_REGEX = "(\\{(@[0-9]+\\$ID_NAME_DELIMITER$ALLOWED_CHARS_REGEX+@)?\\})"
    private const val READ_CONTENT_ONLY_REGEX = "(?<=\\{)(@[0-9]+\\$ID_NAME_DELIMITER$ALLOWED_CHARS_REGEX+@)?(?=\\})"

    private val tokenizerFullEditPattern = Pattern.compile(TOKENIZER_FULL_EDIT_REGEX)
    private val tokenizerContentOnlyPattern = Pattern.compile(TOKENIZER_CONTENT_ONLY_REGEX)

    private val readFullPattern = Pattern.compile(READ_FULL_REGEX)
    private val readContentOnlyPattern = Pattern.compile(READ_CONTENT_ONLY_REGEX)

    /**
     * Create mention tag with format <mention>$params</mention>
     * @param mentioned the text to be enclosed by <mention> tag
     */
    fun createMentionTag(mentioned: CharSequence): CharSequence {
        return "$OPENING_MENTION_TAG$mentioned$CLOSING_MENTION_TAG"
    }

    fun spanText(text: CharSequence, @ColorInt mentionColor: Int, onMentionClicked: MentionSpan.OnClickListener, isFromEdit: Boolean): Spannable {
        val spannableString = SpannableStringBuilder(text)
        val matcher = if (isFromEdit) tokenizerFullEditPattern.matcher(text) else readFullPattern.matcher(text)
        while (matcher.find()) {
            val textToBeReplaced = matcher.group()
            val mentionSpan = getMentionSpanFromTag(textToBeReplaced, mentionColor, matcher.start(), onMentionClicked, isFromEdit)
            if (mentionSpan != null) {
                val startIndex = spannableString.indexOf(textToBeReplaced)
                val endIndex = startIndex + textToBeReplaced.length

                spannableString.setSpan(mentionSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                if (isFromEdit) {
                    spannableString.replace(
                            startIndex + 1,
                            endIndex,
                            mentionSpan.fullName)
                } else {
                    spannableString.replace(
                            startIndex,
                            endIndex,
                            mentionSpan.displayedText
                    )
                }

            }
        }

        return spannableString
    }

    private fun getMentionSpanFromTag(tag: String, mentionColor: Int, start: Int, onMentionClicked: MentionSpan.OnClickListener, isFromEdit: Boolean): MentionSpan? {
        val user = getMentionableUserViewModelFromTokenizerText(tag, isFromEdit)
        return user?.let {
            MentionSpan(
                    color = mentionColor,
                    fullText = user.toString(),
                    fullName = user.fullName,
                    userId = user.id,
                    start = start,
                    onClickListener = onMentionClicked
            )
        }
    }

    fun getAllMentionSpansFromText(text: Spannable, start: Int = 0, end: Int = text.length): Array<MentionSpan> {
        return text.getSpans(start, end, MentionSpan::class.java)
    }

    fun stripInvalidMentionFromText(text: Spannable, existingSpanList: Iterable<MentionSpan>): Spannable {
        val spannableStringBuilder = SpannableStringBuilder(text)
        existingSpanList.forEach { span ->
            val spanStart = text.getSpanStart(span)
            val spanEnd = text.getSpanEnd(span)

            if (spanEnd - spanStart != span.length) {
                val replacingText = text.substring(spanStart, spanEnd)
                spannableStringBuilder.replace(
                        spanStart,
                        spanEnd,
                        ""
                )
                if (spanStart != text.indexOf(replacingText) ||
                        replacingText.length != span.length - 1)
                    spannableStringBuilder.insert(spanStart, replacingText)
            }
        }

        return spannableStringBuilder
    }

    private fun getMentionableUserViewModelFromTokenizerText(text: String, isFromEdit: Boolean): MentionableUserViewModel? {
        val contentMatcher: Matcher = if (isFromEdit) tokenizerContentOnlyPattern.matcher(text) else readContentOnlyPattern.matcher(text)
        return if (contentMatcher.find()) {
            val content = contentMatcher.group()
            val splittedContent = content.split(ID_NAME_DELIMITER)
            val userId: String = splittedContent.first().replace(MENTION_CHAR, "")
            val fullName: String = splittedContent.last().replace(MENTION_CHAR, "")

            MentionableUserViewModel(
                    id = userId,
                    userName = "",
                    fullName = fullName,
                    avatarUrl = ""
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
            } catch (e: Exception) {
            }
        }

        return spannableStringBuilder.toString()
    }
}