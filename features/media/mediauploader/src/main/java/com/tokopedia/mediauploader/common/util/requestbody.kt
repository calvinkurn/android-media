package com.tokopedia.mediauploader.common.util

import com.tokopedia.mediauploader.common.state.ProgressUploader
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File

fun String.requestBody(): RequestBody {
    return RequestBody.create(MultipartBody.FORM, this)
}

fun File.fileBody(
    type: String,
    bodyName: String,
    progressUploader: ProgressUploader? = null
): MultipartBody.Part {
    val contentType = type.toMediaTypeOrNull()
    val requestBody = UploadRequestBody(this, contentType, progressUploader)
    return MultipartBody.Part.createFormData(bodyName, this.name, requestBody)
}

fun ByteArray.byteBody(
    fileName: String,
    type: String,
    bodyName: String
): MultipartBody.Part {
    val contentType = type.toMediaTypeOrNull()
    val requestBody = RequestBody.create(contentType, this)
    return MultipartBody.Part.createFormData(bodyName, fileName, requestBody)
}