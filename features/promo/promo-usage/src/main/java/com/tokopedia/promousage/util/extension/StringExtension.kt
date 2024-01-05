package com.tokopedia.promousage.util.extension

import android.content.Context
import com.tokopedia.unifycomponents.HtmlLinkHelper

internal fun String.toSpannableHtmlString(context: Context): CharSequence? {
    val text = this.replace("&amp;", "&").replace("<a>", "<a href=\"\">")
    return HtmlLinkHelper(context, text).spannedString
}
