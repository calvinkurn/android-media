package com.tokopedia.shareexperience.domain.model

enum class ShareExMimeTypeEnum(val nameType: String) {
    TEXT("text/plain"),
    IMAGE("image/*"),
    ALL("*/*"),
    NOTHING("")
}
