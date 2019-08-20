package com.tokopedia.tradein

import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils

object Utils {
    @JvmStatic
    fun getHtmlFormat(text: String?): String {
        if (TextUtils.isEmpty(text)) {
            return SpannableStringBuilder("").toString()
        }
        val replacedText = text?.replace("&amp;", "&")
        val result: Spanned
        result = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> Html.fromHtml(replacedText, Html.FROM_HTML_MODE_LEGACY)
            else -> Html.fromHtml(replacedText)
        }
        return result.toString()
    }
}