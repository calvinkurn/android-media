package com.tokopedia.shop.settings.common.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.unifycomponents.ticker.Ticker

fun Ticker.getDescriptionWithSpannable(color: Int, descriptionText: String, endOfText: String, clickText :() -> Unit): SpannableStringBuilder {
    val spannableText = SpannableString(endOfText)
    val startIndex = 0
    val endIndex = spannableText.length
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            clickText()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = color
            ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
    }
    spannableText.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return SpannableStringBuilder(descriptionText).append(" ").append(spannableText).append(".")
}