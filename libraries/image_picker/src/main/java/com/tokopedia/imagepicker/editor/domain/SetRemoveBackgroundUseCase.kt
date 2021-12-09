package com.tokopedia.imagepicker.editor.domain

import com.tokopedia.imagepicker.editor.data.NetworkServices
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import rx.Observable
import java.io.File
import javax.inject.Inject

class SetRemoveBackgroundUseCase @Inject constructor(
    private val services: NetworkServices
) {

//    fun invoke(params: String): Observable<ResponseBody> {
//        return services.removeBackground(
//            base64 = params,
//            size = COMPRESS_SIZE_TYPE,
//            apiKey = API_KEY
//        )
//    }
//
//    fun File.fileBody(
//        type: String,
//        bodyName: String
//    ): MultipartBody.Part {
//        val contentType = MediaType.parse(type)
//        val requestBody = RequestBody.create(contentType, this)
//        return MultipartBody.Part.createFormData(bodyName, this.name, requestBody)
//    }
//
//    companion object {
//        private const val API_KEY = "gaq7AqrqmZK7YFYApsTm6Jx3" //StU7eRw6z5tmPsmG61tbeHKX
//        private const val COMPRESS_SIZE_TYPE = "preview"
//    }

    fun invoke(params: File): Observable<ResponseBody> {
        return services.removeBackground(params.fileBody())
    }

    private fun File.fileBody(): MultipartBody.Part {
        val contentType = MediaType.parse(SUPPORTED_CONTENT_TYPE)
        val requestBody = RequestBody.create(contentType, this)
        return MultipartBody.Part.createFormData(BODY_FILE_UPLOAD, this.name, requestBody)
    }

    companion object {
        private const val BODY_FILE_UPLOAD = "file"
        private const val SUPPORTED_CONTENT_TYPE = "image/*"
    }

}