package com.tokopedia.mediauploader.video.data.params

import com.tokopedia.mediauploader.common.util.fileBody
import java.io.File

data class ChunkUploadParam(
    var sourceId: String,
    var uploadId: String,
    var partNumber: String,
    var file: File,
    var timeOut: String,
) {

    fun fileBody() = file.fileBody(
        type = TYPE_FILE,
        bodyName = BODY_FILE
    )

    companion object {
        private const val BODY_FILE = "file"
        private const val TYPE_FILE = "video/*"
    }
}