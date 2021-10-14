package com.tokopedia.mediauploader.image.data.params

import com.tokopedia.mediauploader.common.state.ProgressCallback
import com.tokopedia.mediauploader.common.util.fileBody
import java.io.File

data class ImageUploaderParam(
    var hostUrl: String,
    var sourceId: String,
    var file: File,
    var timeOut: String
) {

    fun hasNoParams(): Boolean {
        return hostUrl.isEmpty() && !file.exists() && timeOut.isEmpty()
    }

    fun fileBody(progressCallback: ProgressCallback?) = file.fileBody(
        type = SUPPORTED_CONTENT_TYPE,
        bodyName = BODY_FILE_UPLOAD,
        progressCallback = progressCallback
    )

    companion object {
        private const val BODY_FILE_UPLOAD = "file_upload"
        private const val SUPPORTED_CONTENT_TYPE = "image/*"
    }

}