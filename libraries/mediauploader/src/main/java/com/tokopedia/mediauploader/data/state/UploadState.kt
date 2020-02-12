package com.tokopedia.mediauploader.data.state

enum class UploadState {
    NOT_FOUND,
    FILE_MAX_SIZE,
    TINY_RESOLUTION,
    BIG_RESOLUTION,
    EXT_NOT_ALLOWED,
    UPLOAD_ERROR
}