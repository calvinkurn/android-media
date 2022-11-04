package com.tokopedia.journeydebugger.helper

fun formatDataExcerpt(raw: String?) : String {

    if (raw == null) return ""

    return raw.substringAfterLast(".")
}

