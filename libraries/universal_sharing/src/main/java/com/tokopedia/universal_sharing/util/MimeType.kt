package com.tokopedia.universal_sharing.util

enum class MimeType(val type: String) {
    TEXT(TYPE_TEXT),
    IMAGE(TYPE_IMAGE),
    ALL(TYPE_ALL)
}

// Mime Type for the intent
private const val TYPE_TEXT = "text/plain"
private const val TYPE_IMAGE = "image/*"
private const val TYPE_ALL = "*/*"
