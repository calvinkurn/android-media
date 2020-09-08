@file:JvmName("SpanTextUtil")

package com.tokopedia.shop.settings.common.util

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan

/**
 * Created by hendry on 16/08/18.
 */
fun getSpandableColorText(strToPut: String, stringToBold: String, color: ForegroundColorSpan): CharSequence {
    if (TextUtils.isEmpty(stringToBold)) {
        return strToPut
    }
    val strToPutLowerCase = strToPut.toLowerCase()
    val strToBoldLowerCase = stringToBold.toLowerCase()
    val spannableStringBuilder = SpannableStringBuilder(strToPut)
    var indexStartBold = strToPutLowerCase.indexOf(strToBoldLowerCase)
    var indexEndBold: Int = -1
    if (indexStartBold != -1) {
        indexEndBold = indexStartBold + stringToBold.length
    }
    if (indexStartBold == -1) {
        return spannableStringBuilder
    } else {
        spannableStringBuilder.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                indexStartBold, indexEndBold, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableStringBuilder.setSpan(color, indexStartBold, indexEndBold,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableStringBuilder
    }
}

fun getSpandableColorText(strToPut: String, stringToBold: String, color: Int): CharSequence {
    val spannableStringBuilder = SpannableStringBuilder(stringToBold)
    val indexStart = 0
    val indexEnd = indexStart + stringToBold.length
    val boldColor = ForegroundColorSpan(color)

    spannableStringBuilder.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            indexStart,
            indexEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    spannableStringBuilder.setSpan(
            boldColor,
            indexStart,
            indexEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return SpannableStringBuilder(strToPut).append(" ").append(spannableStringBuilder).append(".")
}

