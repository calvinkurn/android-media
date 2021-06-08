package com.tokopedia.power_merchant.subscribe.view_old.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.ColorInt

object PowerMerchantSpannableUtil {

    fun createSpannableString(
        text: String,
        highlightText: String,
        @ColorInt colorId: Int,
        isBold: Boolean = false,
        boldTextList: List<String>? = null,
        onClick: (() -> Unit)? = null
    ): SpannableString {
        val spannableString = SpannableString(text)
        val startIndex = text.indexOf(highlightText)
        val endIndex = text.length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                onClick?.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        val colorSpan = ForegroundColorSpan(colorId)
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        if(isBold) {
            val styleSpan = StyleSpan(Typeface.BOLD)
            spannableString.setSpan(styleSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        boldTextList?.forEach {
            val boldStartIndex = text.indexOf(it)
            val boldEndIndex = boldStartIndex + it.length

            val styleSpan = StyleSpan(Typeface.BOLD)
            spannableString.setSpan(styleSpan, boldStartIndex, boldEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return spannableString
    }
}