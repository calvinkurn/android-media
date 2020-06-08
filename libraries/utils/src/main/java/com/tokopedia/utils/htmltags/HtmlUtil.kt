package com.tokopedia.utils.htmltags

import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY

object HtmlUtil {
    const val UL_TAG = "bulletedList"
    const val OL_TAG = "numberedList"
    const val LI_TAG = "listItem"

    fun fromHtml(htmlText: String): Spanned {
        val formattedHtml = htmlText
            .replace("(?i)<ul[^>]*>".toRegex(), "<$UL_TAG>")
            .replace("(?i)</ul>".toRegex(), "</$UL_TAG>")
            .replace("(?i)<ol[^>]*>".toRegex(), "<$OL_TAG>")
            .replace("(?i)</ol>".toRegex(), "</$OL_TAG>")
            .replace("(?i)<li[^>]*>".toRegex(), "<$LI_TAG>")
            .replace("(?i)</li>".toRegex(), "</$LI_TAG>")

        return HtmlCompat.fromHtml(formattedHtml, FROM_HTML_MODE_LEGACY, null, ListTagHandler())
    }

}
