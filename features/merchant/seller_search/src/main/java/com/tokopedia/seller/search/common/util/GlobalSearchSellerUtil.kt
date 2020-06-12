package com.tokopedia.seller.search.common.util

import android.text.SpannableString
import android.text.TextUtils
import java.util.*

fun indexOfSearchQuery(displayName: String, searchTerm: String): Int {
    return if (!TextUtils.isEmpty(searchTerm)) {
        displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()))
    } else -1
}

internal fun SpannableString.safeSetSpan(what: Any, start: Int, end: Int, flags: Int) {
    try {
        setSpan(what, start, end, flags)
    }
    catch (throwable: Throwable) {
        throwable.printStackTrace()
    }
}