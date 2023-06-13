package com.tokopedia.affiliate.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.text.style.MetricAffectingSpan
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.unifyprinciples.Typography

private class BoldTypefaceSpan(private val typeface: Typeface?) : MetricAffectingSpan() {
    override fun updateDrawState(paint: TextPaint) {
        paint.typeface = typeface
    }

    override fun updateMeasureState(paint: TextPaint) {
        paint.typeface = typeface
    }
}

fun Typography.setBoldSpannedText(
    str: String, start: Int, end: Int, boldTypography: Int
) {
    val sb = SpannableString(str)
    val endSpan = start + end
    if (start >= 0 && endSpan < str.length) {
        context?.let { ctx ->
            val boldFont = Typography.getFontType(ctx, true, boldTypography)
            sb.setSpan(
                BoldTypefaceSpan(boldFont!!), start, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            sb.setSpan(
                ForegroundColorSpan(
                    MethodChecker.getColor(
                        context, com.tokopedia.unifyprinciples.R.color.Unify_NN950
                    )
                ),
                start,
                endSpan,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    text = sb
}
