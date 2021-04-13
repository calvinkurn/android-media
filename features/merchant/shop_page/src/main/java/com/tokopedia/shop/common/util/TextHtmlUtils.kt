package com.tokopedia.shop.common.util

import android.os.Build
import android.text.Html

/**
 * Created by nathan on 3/4/18.
 */
object TextHtmlUtils {
    fun getTextFromHtml(htmlText: String?): CharSequence {
        val charSequence: CharSequence
        charSequence = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(htmlText)
        }
        return charSequence
    }
}