package com.tokopedia.shareexperience.domain.model

enum class ShareExMimeTypeEnum(val textType: String) {
    TEXT("text/plain"),
    IMAGE("image/jpeg"),
    ALL("*/*"),
    NOTHING("")
}
