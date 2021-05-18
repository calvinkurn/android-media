package com.tkpd.remoteresourcerequest.utils

object Constants {

    const val URL_SEPARATOR = "/"
    /*
     We don't want to depend on context since it is held in WeakReference. So if somehow context
     get lost then also we have to ensure these messages to get displayed. Hence keeping it here.
     */
    const val CONTEXT_NOT_INITIALIZED_MESSAGE = "ResourceDownloadManager not initialized!! " +
            "\nCall ResourceDownloadManager.initialize(context, resId) first before proceeding!!!!"
    const val URL_NOT_INITIALIZED_MESSAGE = "ResourceDownloadManager not initialized!! " +
            "\nCall ResourceDownloadManager.setBaseAndRelativeUrl(baseUrl, relativeUrl) " +
            "at least once!!!!"
    const val CORRUPT_FILE_MESSAGE = "Corrupt downloaded file!!"

    const val MULTI_DPI_ARRAY = "multiDpi"
    const val SINGLE_DPI_ARRAY = "singleDpi"
    const val NO_DPI_ARRAY = "noDpi"
    const val AUDIO_ARRAY = "audio"

    const val REQUIRE_COMPLETE_URL_ERROR_MSG = "Please provide non empty name or correct completeUrl"

}
