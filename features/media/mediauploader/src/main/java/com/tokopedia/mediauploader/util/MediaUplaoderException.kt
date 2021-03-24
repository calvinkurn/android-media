package com.tokopedia.mediauploader.util

class MediaUplaoderException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
