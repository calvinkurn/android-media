package com.tokopedia.shop.settings.common.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

fun getTextWithSpannable(color: Int, text: String, endOfText: String, onClick: (() -> Unit)? = null): SpannableStringBuilder {
    val spannableText = SpannableString(endOfText)
    val startIndex = 0
    val endIndex = spannableText.length
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClick?.invoke()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = color
            ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
    }
    spannableText.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return SpannableStringBuilder(text).append(" ").append(spannableText).append(".")
}