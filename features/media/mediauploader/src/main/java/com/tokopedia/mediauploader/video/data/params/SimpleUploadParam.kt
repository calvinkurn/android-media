package com.tokopedia.mediauploader.video.data.params

import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.util.network.fileBody
import java.io.File

data class SimpleUploadParam(
    var sourceId: String,
    var file: File,
    var timeOut: String,
) {

    fun fileBody(progressUploader: ProgressUploader?) = file.fileBody(
        type = TYPE_FILE,
        bodyName = BODY_FILE,
        progressUploader = progressUploader
    )

    companion object {
        private const val BODY_FILE = "file"
        private const val TYPE_FILE = "video/*"
    }
}
