package com.tokopedia.common.travel.utils

import android.text.Html

/**
 * Created by furqan on 25/07/19.
 */
object TextHtmlUtils {
    fun getTextFromHtml(htmlText: String): CharSequence {
        val charSequence: CharSequence
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            charSequence = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
        } else {
            charSequence = Html.fromHtml(htmlText)
        }
        return charSequence
    }
}