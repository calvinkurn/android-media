package com.tokopedia.mediauploader.image.data

import com.tokopedia.mediauploader.image.data.entity.ImageUploader
import com.tokopedia.mediauploader.common.util.NetworkTimeOutInterceptor.Companion.HEADER_TIMEOUT
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface ImageUploadServices {

    @Multipart
    @POST suspend fun uploadImage(
        @Url urlToUpload: String,
        @Part partBody: MultipartBody.Part,
        @Header(HEADER_TIMEOUT) timeOut: String
    ) : ImageUploader

}