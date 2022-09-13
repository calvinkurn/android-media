package com.tokopedia.product.detail.common.extensions

import android.content.Context
import com.tokopedia.unifycomponents.HtmlLinkHelper

/**
 * Created by yovi.putra on 13/09/22"
 * Project name: android-tokopedia-core
 **/

fun String?.fromHtml(context: Context) = HtmlLinkHelper(
    context = context,
    htmlString = this.orEmpty().replace(
        regex = "(\r\n|\n)".toRegex(),
        replacement = "<br />"
    )
).spannedString