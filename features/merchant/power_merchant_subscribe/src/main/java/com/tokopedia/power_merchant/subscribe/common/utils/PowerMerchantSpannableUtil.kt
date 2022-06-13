package com.tokopedia.power_merchant.subscribe.common.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
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

    fun Typography.setTextMakeHyperlink(text: String, onClick: () -> Unit) {
        val htmlString = HtmlLinkHelper(context, text)
        this.movementMethod = LinkMovementMethod.getInstance()
        this.highlightColor = Color.TRANSPARENT
        this.text = htmlString.spannedString
        htmlString.urlList.getOrNull(0)?.setOnClickListener {
            onClick()
        }
    }

    fun getColorHexString(context: Context?, idColor: Int): String {
        return try {
            val colorHexInt = context?.let { ContextCompat.getColor(it, idColor) }
            val colorToHexString = colorHexInt?.let {
                Integer.toHexString(it).uppercase().substring(START_INDEX_HEX_STRING)
            }
            return "#$colorToHexString"
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private const val START_INDEX_HEX_STRING = 2
}