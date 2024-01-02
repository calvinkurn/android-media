package com.tokopedia.catalogcommon.util



import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalogcommon.R

fun SpannableStringBuilder.setSpanOnText(
    text: String,
    vararg spans: Any,
) {
    val start = indexOf(text)
    val end = start + text.length

    spans.forEach {
        setSpan(it, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

fun getClickableSpan(onClick: () -> Unit): ClickableSpan {
    return object : ClickableSpan() {
        override fun onClick(p0: View) {
            onClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }
}

fun getBoldSpan() = StyleSpan(Typeface.BOLD)

fun getGreenColorSpan(context: Context) = ForegroundColorSpan(MethodChecker.getColor(context, R.color.dms_static_green_GN500))
