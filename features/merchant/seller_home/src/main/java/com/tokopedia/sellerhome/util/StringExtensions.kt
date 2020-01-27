package com.tokopedia.sellerhome.util

import android.text.Html

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