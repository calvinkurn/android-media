package com.tokopedia.shareexperience.data.util

import org.json.JSONArray

fun JSONArray.toArray(): Array<String> {
    return Array(this.length()) { i ->
        this.getString(i)
    }
}
