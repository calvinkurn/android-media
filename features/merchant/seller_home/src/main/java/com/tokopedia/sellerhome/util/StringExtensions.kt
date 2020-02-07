package com.tokopedia.sellerhome.util

import android.text.Html
import com.tokopedia.sellerhome.util.DateUtil.getFormattedDate
import java.util.*

/**
 * Created By @ilhamsuaib on 2020-01-22
 */

fun String.asUpperCase(): String {
    val locale = Locale("id")
    return this.toUpperCase(locale)
}

fun String.parseAsHtml(): CharSequence {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

fun String.parseDateTemplate(): CharSequence {

    val regex = mapOf(
            "{DATE_YESTERDAY_PAST_7D}" to { getFormattedDate(7, "dd MMM yy").asUpperCase() },
            "{DATE_YESTERDAY}" to { getFormattedDate(1, "dd MMM yy").asUpperCase() }
    )

    val pattern = "\\{([^}]*?)\\}".toRegex()
    return pattern.replace(this) {
        regex[it.value]?.invoke() ?: it.value
    }
}