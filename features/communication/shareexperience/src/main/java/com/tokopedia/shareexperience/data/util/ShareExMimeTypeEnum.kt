package com.tokopedia.shareexperience.data.util
enum class ShareExMimeTypeEnum(val nameType: String) {
    TEXT(TYPE_TEXT),
    IMAGE(TYPE_IMAGE),
    ALL(TYPE_ALL),
    NOTHING("")
}

// Mime Type for the intent
private const val TYPE_TEXT = "text/plain"
private const val TYPE_IMAGE = "image/*"
private const val TYPE_ALL = "*/*"
