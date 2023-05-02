package com.tokopedia.feedcomponent.util.caption

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.*
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.text.buildSpannedString
import androidx.core.text.toSpanned
import com.tokopedia.feedcomponent.util.TagConverter
import com.tokopedia.feedcomponent.util.safeSetSpan
import com.tokopedia.kotlin.extensions.view.toZeroIfNull

/**
 * Created by meyta.taliti on 02/01/23.
 */
class FeedCaption private constructor(
    captionText: String,
    private val author: Author?,
    private val tag: Tag?,
    private val readMore: ReadMore?
) {

    companion object {
        /**
         * based on: unify_principles/src/main/java/com/tokopedia/unifyprinciples/Typography.kt
         */
        const val ROBOTO_BOLD = "RobotoBold.ttf"
    }

    private var spannableCaption: Spanned = SpannableString(captionText.trim())

    private val tagConverter = TagConverter()

    init {
        convertTag()
        concatAuthorWithCaption()
        trimCaption()
    }

    @SuppressLint("PII Data Exposure")
    private fun concatAuthorWithCaption() {
        val author = author ?: return
        if (author.name.isEmpty()) return

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                author.clickListener()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.typeface = author.typeface
                ds.color = author.colorRes
            }
        }
        val authorSpanned = SpannableString(author.name).apply {
            safeSetSpan(
                clickableSpan,
                0,
                author.name.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            safeSetSpan(
                StyleSpan(Typeface.BOLD),
                0,
                author.name.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }.toSpanned()
        spannableCaption = buildSpannedString {
            append(authorSpanned)
            append(" - ")
            append(spannableCaption)
        }.toSpanned()
    }

    private fun convertTag() {
        val tag = tag ?: return
        spannableCaption = tagConverter.convertToLinkifyHashtag(
            SpannableString(spannableCaption),
            tag.colorRes,
            onClick = {
                tag.clickListener(it)
            }
        ).toSpanned()
    }

    private fun trimCaption() {
        val readMore = readMore ?: return

        val maxTrimChar = readMore.maxTrimChar.toZeroIfNull()
        if (spannableCaption.toString().length < maxTrimChar) return

        val readMoreClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                readMore.clickListener()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = readMore.colorRes
            }
        }

        val shortCaption = spannableCaption.subSequence(0, maxTrimChar)
        val withReadMore = SpannableStringBuilder()
            .append(shortCaption)
            .append("...")
            .append(readMore.label)
        spannableCaption = SpannableString.valueOf(withReadMore).also {
            it.safeSetSpan(
                readMoreClickableSpan,
                maxTrimChar,
                it.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }.toSpanned()
    }

    fun toSpan(): Spanned {
        return spannableCaption
    }

    data class Builder(val captionText: String) {

        private var author: Author? = null
        private var tag: Tag? = null
        private var readMore: ReadMore? = null

        fun withAuthor(author: Author) = apply {
            this.author = author
        }

        fun withTag(tag: Tag) = apply {
            this.tag = tag
        }

        fun trimCaption(readMore: ReadMore) = apply {
            this.readMore = readMore
        }

        fun build(): Spanned = FeedCaption(
            captionText,
            author,
            tag,
            readMore
        ).toSpan()
    }

    data class Author(
        val name: String,
        val colorRes: Int,
        val typeface: Typeface?,
        val clickListener: () -> Unit
    )

    data class Tag(
        val colorRes: Int,
        val clickListener: (String) -> Unit
    )

    data class ReadMore(
        val maxTrimChar: Int?,
        val label: String,
        val colorRes: Int,
        val clickListener: () -> Unit
    )

}
