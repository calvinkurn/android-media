package com.tokopedia.shopdiscount.utils.formatter

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.tokopedia.abstraction.common.utils.view.MethodChecker

object SpannableHelper {

    fun setSpannedColorString(
        context: Context?,
        spannableString: SpannableString,
        stringToBeColorSpanned: String,
        colorRes: Int
    ) {
        val color = MethodChecker.getColor(context, colorRes)
        val foregroundColorSpan = ForegroundColorSpan(color)
        spannableString.apply {
            setSpan(
                foregroundColorSpan,
                spannableString.indexOf(stringToBeColorSpanned),
                spannableString.indexOf(stringToBeColorSpanned) + stringToBeColorSpanned.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
    }
}
