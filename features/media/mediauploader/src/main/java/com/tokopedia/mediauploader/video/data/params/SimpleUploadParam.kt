package com.tokopedia.mediauploader.video.data.params

import com.tokopedia.mediauploader.common.state.ProgressCallback
import com.tokopedia.mediauploader.common.util.fileBody
import java.io.File

data class SimpleUploadParam(
    var sourceId: String,
    var file: File,
    var timeOut: String,
) {

    fun fileBody(progressCallback: ProgressCallback?) = file.fileBody(
        type = TYPE_FILE,
        bodyName = BODY_FILE,
        progressCallback = progressCallback
    )

    companion object {
        private const val BODY_FILE = "file"
        private const val TYPE_FILE = "video/*"
    }
}