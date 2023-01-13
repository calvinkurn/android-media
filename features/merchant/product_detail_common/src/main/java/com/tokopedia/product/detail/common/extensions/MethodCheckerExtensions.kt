package com.tokopedia.product.detail.common.extensions

import android.content.Context
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.unifycomponents.HtmlLinkHelper

/**
 * Created by yovi.putra on 13/09/22"
 * Project name: android-tokopedia-core
 **/

fun String.parseAsHtmlLink(context: Context, replaceNewLine: Boolean = true) = HtmlLinkHelper(
    context = context,
    htmlString = if (replaceNewLine) {
        this.replace("(\r\n|\n)".toRegex(), "<br />")
    } else {
        this
    }
).spannedString

fun Context.getColorChecker(id: Int) = MethodChecker.getColor(this, id)
fun Context.getDrawableChecker(id: Int) = MethodChecker.getDrawable(this, id)
