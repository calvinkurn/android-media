package com.tokopedia.mediauploader.util

import timber.log.Timber
import java.io.File

private const val TIMBER_TAG = "P1#MEDIA_UPLOADER_ERROR#"

fun trackToTimber(filePath: File? = null, sourceId: String, message: String) {
    if (filePath != null && filePath.path.isNotEmpty()) {
        trackToTimber(sourceId, "Error upload image %s because %s".format(filePath.path, message))
    }
}

fun trackToTimber(sourceId: String = "", errorMessage: String) {
    Timber.w("$TIMBER_TAG$sourceId;err='$errorMessage'")
}