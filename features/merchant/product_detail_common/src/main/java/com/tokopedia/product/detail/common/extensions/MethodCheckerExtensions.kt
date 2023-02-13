package com.tokopedia.product.detail.common.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
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

fun Context.getColorChecker(id: Int) = try {
    MethodChecker.getColor(this, id)
} catch (t: Throwable) {
    t.printStackTrace()
    0
}

fun Context.getDrawableChecker(id: Int): Drawable? = try {
    ContextCompat.getDrawable(this, id)
} catch (t: Throwable) {
    t.printStackTrace()
    null
}
