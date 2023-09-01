package com.tokopedia.promousage.util.extension

import android.content.Context
import com.tokopedia.unifycomponents.HtmlLinkHelper

internal fun String.toSpannableHtmlString(context: Context): CharSequence? {
    return HtmlLinkHelper(context, replace("<a>", "<a href=\"\">")).spannedString
}
