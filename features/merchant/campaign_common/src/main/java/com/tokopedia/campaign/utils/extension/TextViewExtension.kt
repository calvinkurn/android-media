package com.tokopedia.campaign.utils.extension

import android.graphics.Paint
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat

fun AppCompatTextView.strikethrough() {
    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun AppCompatTextView.setTextColorCompat(@ColorRes resourceId: Int) {
    val color = ContextCompat.getColor(this.context, resourceId)
    this.setTextColor(color)
}

/**
 * The function will finds the specified substring and convert it to a hyperlink
 *
 * @param fullText: Any kind of your lengthy text.
 * @param hyperlinkSubstring: The substring that will by converted to a hyperlink. The substring must exist in the fullText.
 * Otherwise, no substring will be converted to hyperlink
 * @param onHyperlinkClick: Block of code that will be triggered if any of the hyperlink substring is clicked
 */
fun AppCompatTextView.setHyperlinkText(fullText: String, hyperlinkSubstring: String, onHyperlinkClick: () -> Unit) {
    val spannableString = SpannableString(fullText)
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            onHyperlinkClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            ds.isUnderlineText = false
        }
    }

    val boldSpan = StyleSpan(Typeface.BOLD)
    val substringIndexes = findFirstAndLastIndexOfSubstring(fullText, hyperlinkSubstring)
    val (substringFirstIndex, substringLastIndex) = substringIndexes

    try {
        spannableString.setSpan(
            boldSpan,
            substringFirstIndex,
            substringLastIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            clickableSpan,
            substringFirstIndex,
            substringLastIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        this.text = spannableString
        this.movementMethod = LinkMovementMethod.getInstance()
    } catch (e: Exception) {}

}

private fun findFirstAndLastIndexOfSubstring(text: String, textSubstring: String): Pair<Int, Int> {
    val start = text.indexOf(textSubstring)
    return if (start != -1) {
        Pair(start, start + textSubstring.length)
    } else {
        Pair(-1, -1)
    }
}