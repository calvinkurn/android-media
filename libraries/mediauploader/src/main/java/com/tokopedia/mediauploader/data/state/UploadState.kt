package com.tokopedia.mediauploader.data.state

enum class UploadState {
    FILE_NOT_FOUND,
    FILE_MAX_SIZE,
    TINY_RESOLUTION,
    BIG_RESOLUTION,
    EXTENSION_DISALLOWED
}