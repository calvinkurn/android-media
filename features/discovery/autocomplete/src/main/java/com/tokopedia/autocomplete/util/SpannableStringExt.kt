package com.tokopedia.autocomplete.util

import android.text.SpannableString

internal fun SpannableString.safeSetSpan(what: Any, start: Int, end: Int, flags: Int) {
    try {
        setSpan(what, start, end, flags)
    }
    catch (throwable: Throwable) {
        throwable.printStackTrace()
    }
}