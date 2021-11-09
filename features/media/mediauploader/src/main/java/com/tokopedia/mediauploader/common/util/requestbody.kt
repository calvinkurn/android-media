package com.tokopedia.mediauploader.common.util

import com.tokopedia.mediauploader.common.state.ProgressUploader
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

fun String.requestBody(): RequestBody {
    return RequestBody.create(MultipartBody.FORM, this)
}

fun File.fileBody(
    type: String,
    bodyName: String,
    progressUploader: ProgressUploader? = null
): MultipartBody.Part {
    val contentType = MediaType.parse(type)
    val requestBody = UploadRequestBody(this, contentType, progressUploader)
    return MultipartBody.Part.createFormData(bodyName, this.name, requestBody)
}

fun ByteArray.byteBody(
    fileName: String,
    type: String,
    bodyName: String
): MultipartBody.Part {
    val contentType = MediaType.parse(type)
    val requestBody = RequestBody.create(contentType, this)
    return MultipartBody.Part.createFormData(bodyName, fileName, requestBody)
}