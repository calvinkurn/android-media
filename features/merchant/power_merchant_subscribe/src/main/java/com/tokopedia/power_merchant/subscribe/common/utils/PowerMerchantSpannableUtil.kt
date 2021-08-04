package com.tokopedia.power_merchant.subscribe.common.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.ColorInt
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import timber.log.Timber

object PowerMerchantSpannableUtil {

    /**
     * this is mandatory if u want to use this method
     * to make your text view clickable
     * textView.setMovementMethod(LinkMovementMethod.getInstance())
     * */
    fun createSpannableString(
        text: CharSequence,
        highlightText: String,
        @ColorInt colorId: Int,
        isBold: Boolean = false,
        boldTextList: List<String>? = null,
        onClick: (() -> Unit)? = null
    ): SpannableString {
        val spannableString = SpannableString(text)
        val startIndex = text.indexOf(highlightText)
        val endIndex = startIndex + highlightText.length

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
        try {
            spannableString.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } catch (e: IndexOutOfBoundsException) {
            Timber.e(e)
        }

        if(isBold) {
            val styleSpan = StyleSpan(Typeface.BOLD)
            try {
                spannableString.setSpan(styleSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            } catch (e: IndexOutOfBoundsException) {
                Timber.e(e)
            }
        }

        boldTextList?.forEach {
            val boldStartIndex = text.indexOf(it)
            val boldEndIndex = boldStartIndex + it.length

            val styleSpan = StyleSpan(Typeface.BOLD)
            try {
                spannableString.setSpan(styleSpan, boldStartIndex, boldEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            } catch (e: IndexOutOfBoundsException) {
                Timber.e(e)
            }
        }

        return spannableString
    }
}