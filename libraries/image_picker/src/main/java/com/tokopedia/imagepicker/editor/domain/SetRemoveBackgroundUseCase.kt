package com.tokopedia.imagepicker.editor.domain

import com.tokopedia.imagepicker.editor.data.NetworkServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import rx.Observable
import java.io.File
import javax.inject.Inject

class SetRemoveBackgroundUseCase @Inject constructor(
    private val services: NetworkServices
) {

    fun invoke(params: File): Observable<ResponseBody> {
        return services.removeBackground(params.fileBody())
    }

    private fun File.fileBody(): MultipartBody.Part {
        val contentType = SUPPORTED_CONTENT_TYPE.toMediaType()
        val requestBody = RequestBody.create(contentType, this)
        return MultipartBody.Part.createFormData(BODY_FILE_UPLOAD, this.name, requestBody)
    }

    companion object {
        private const val BODY_FILE_UPLOAD = "file"
        private const val SUPPORTED_CONTENT_TYPE = "image/*"
    }

}