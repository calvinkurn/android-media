package com.tokopedia.mediauploader.image.data

import com.tokopedia.mediauploader.common.util.NetworkTimeOutInterceptor.Companion.HEADER_TIMEOUT
import com.tokopedia.mediauploader.image.data.entity.ImageUploader
import okhttp3.MultipartBody
import retrofit2.http.*

interface ImageUploadServices {

    @Multipart
    @POST
    suspend fun uploadImage(
        @Url urlToUpload: String,
        @Part partBody: MultipartBody.Part,
        @Header(HEADER_TIMEOUT) timeOut: String
    ): ImageUploader

}