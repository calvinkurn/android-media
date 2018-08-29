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
    var indexStartBold = -1
    var indexEndBold = -1
    if (TextUtils.isEmpty(stringToBold)) {
        return strToPut
    }
    val strToPutLowerCase = strToPut.toLowerCase()
    val strToBoldLowerCase = stringToBold.toLowerCase()
    val spannableStringBuilder = SpannableStringBuilder(strToPut)
    indexStartBold = strToPutLowerCase.indexOf(strToBoldLowerCase)
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
