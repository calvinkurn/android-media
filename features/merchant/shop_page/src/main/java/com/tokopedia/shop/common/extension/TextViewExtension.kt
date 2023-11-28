package com.tokopedia.shop.common.extension

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

private const val SUBSTRING_INDEX_NOT_FOUND = -1

fun TextView.setHyperlinkText(
    fullText: String,
    hyperlinkSubstring: String,
    ignoreCase: Boolean = true,
    onHyperlinkClick: () -> Unit = {}
) {
    val spannableString = SpannableString(fullText)
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            onHyperlinkClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500)
            ds.isUnderlineText = false
        }
    }

    val boldSpan = StyleSpan(Typeface.BOLD)
    val substringIndexes = findFirstAndLastIndexOfSubstring(fullText, hyperlinkSubstring, ignoreCase)
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
    } catch (e: Exception) {
        FirebaseCrashlytics.getInstance().recordException(e)
    }

}

private fun findFirstAndLastIndexOfSubstring(text: String, textSubstring: String, ignoreCase: Boolean): Pair<Int, Int> {
    val start = text.indexOf(textSubstring, ignoreCase = ignoreCase)
    return if (start != SUBSTRING_INDEX_NOT_FOUND) {
        Pair(start, start + textSubstring.length)
    } else {
        Pair(SUBSTRING_INDEX_NOT_FOUND, SUBSTRING_INDEX_NOT_FOUND)
    }
}
