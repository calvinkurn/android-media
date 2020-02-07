package com.tokopedia.sellerhome.util

import android.text.Html
import com.tokopedia.sellerhome.util.DateUtil.getFormattedDate

/**
 * Created By @ilhamsuaib on 2020-01-22
 */

fun String.parseAsHtml(): CharSequence {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

fun String.parseRegex(): CharSequence {
    val pattern = "\\{([^}]*?)\\}".toRegex()
    return pattern.replace(this) {
        regex[it.value]?.invoke() ?: it.value
    }
}

val regex = mapOf(
        "{DATE_YESTERDAY_PAST_7D}" to { getFormattedDate(8, "dd MMM yy").toUpperCase() },
        "{DATE_YESTERDAY}" to { getFormattedDate(1, "dd MMM yy").toUpperCase() }
)