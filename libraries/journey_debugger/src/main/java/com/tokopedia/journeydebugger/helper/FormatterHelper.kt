package com.tokopedia.journeydebugger.helper

fun formatDataExcerpt(raw: String?) : String {

    if (raw == null) return ""

    val dataExcerpt = raw.replace("\\s+".toRegex(), " ")
    if (dataExcerpt.length > 100) {
        return dataExcerpt.substring(0, 100) + "..."
    } else {
        return dataExcerpt
    }
}

