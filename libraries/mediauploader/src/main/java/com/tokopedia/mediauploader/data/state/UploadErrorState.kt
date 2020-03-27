package com.tokopedia.mediauploader.data.state

enum class UploadErrorState {
    NOT_FOUND,
    MAX_SIZE,
    TINY_RES,
    LARGE_RES,
    EXT_ISSUE,
    TIME_OUT,
    NETWORK_ERROR
}