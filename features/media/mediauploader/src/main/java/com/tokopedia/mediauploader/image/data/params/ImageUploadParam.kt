package com.tokopedia.mediauploader.image.data.params

import android.annotation.SuppressLint
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.util.fileBody
import java.io.File

@SuppressLint("ParamFieldAnnotation")
data class ImageUploadParam(
    var sourceId: String,
    var timeOut: String,
    var hostUrl: String,
    var file: File,
    var isSecure: Boolean = false,
    var extraHeader: Map<String, String>,
    var extraBody: Map<String, String>
) {

    fun hasNoParams(): Boolean {
        return hostUrl.isEmpty() && !file.exists() && timeOut.isEmpty()
    }

    fun fileBody(progressUploader: ProgressUploader?) = file.fileBody(
        type = SUPPORTED_CONTENT_TYPE,
        bodyName = BODY_FILE_UPLOAD,
        progressUploader = progressUploader
    )

    companion object {
        private const val BODY_FILE_UPLOAD = "file_upload"
        private const val SUPPORTED_CONTENT_TYPE = "image/*"
    }

}
