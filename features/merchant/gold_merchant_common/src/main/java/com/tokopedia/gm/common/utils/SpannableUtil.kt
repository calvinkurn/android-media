package com.tokopedia.gm.common.utils

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 10/05/23.
 */

object SpannableUtil {

    private const val START_INDEX_HEX_STRING = 2

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
                ds.color = colorId
                ds.isUnderlineText = false
            }
        }
        runCatching {
            spannableString.setSpan(
                clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        if (isBold) {
            val styleSpan = StyleSpan(Typeface.BOLD)
            runCatching {
                spannableString.setSpan(
                    styleSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        boldTextList?.forEach {
            val boldStartIndex = text.indexOf(it)
            val boldEndIndex = boldStartIndex + it.length

            val styleSpan = StyleSpan(Typeface.BOLD)
            runCatching {
                spannableString.setSpan(
                    styleSpan, boldStartIndex, boldEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        return spannableString
    }

    fun getColorHexString(context: Context?, idColor: Int): String {
        runCatching {
            val colorHexInt = context?.let { ContextCompat.getColor(it, idColor) }
            val colorToHexString = colorHexInt?.let {
                Integer.toHexString(it).uppercase().substring(START_INDEX_HEX_STRING)
            }
            return "#$colorToHexString"
        }
        return String.EMPTY
    }
}